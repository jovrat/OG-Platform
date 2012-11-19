/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opengamma.DataNotFoundException;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.core.change.DummyChangeManager;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.target.ComputationTargetSpecificationResolver;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.engine.view.calc.ViewCycle;
import com.opengamma.engine.view.compilation.CompiledViewDefinition;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.Pair;

/**
 * Grid for displaying analytics data for a portfolio or for calculated values that aren't associated with the
 * portfolio (primitives). This class isn't thread safe.
 */
/* package */ abstract class MainAnalyticsGrid<V extends MainGridViewport> extends AnalyticsGrid<V> {

  /** Row and column structure of the grid. */
  protected final MainGridStructure _gridStructure;
  /** Type of data in the grid, portfolio or primitives. */
  private final AnalyticsView.GridType _gridType;
  /** Dependency graph grids for cells in this grid, keyed by grid ID. */
  private final Map<Integer, DependencyGraphGrid> _depGraphs = Maps.newHashMap();
  /** For looking up calculation targets using their specifications. */
  private final ComputationTargetResolver _targetResolver;

  /** Cache of results. */
  protected ResultsCache _cache = new ResultsCache();
  /** The calculation cycle used to calculate the most recent set of results. */
  private ViewCycle _cycle = EmptyViewCycle.INSTANCE;

  /* package */ MainAnalyticsGrid(AnalyticsView.GridType gridType,
                                  MainGridStructure gridStructure,
                                  String gridId,
                                  ComputationTargetResolver targetResolver) {
    super(gridId);
    ArgumentChecker.notNull(gridType, "gridType");
    ArgumentChecker.notNull(gridStructure, "gridStructure");
    ArgumentChecker.notNull(targetResolver, "targetResolver");
    _gridType = gridType;
    _gridStructure = gridStructure;
    _targetResolver = targetResolver;
  }

  /**
   * Updates the data in the viewports of the main grid and all dependency graph grids when new results arrive
   * from the calculation engine.
   * @param cache Cache of calculation results
   * @param cycle Calculation cycle that calculated the latest results
   * @return List of IDs specifiying the viewports whose data has changed as a result of the new update
   */
  /* package */ List<String> updateResults(ResultsCache cache, ViewCycle cycle) {
    _cache = cache;
    _cycle = cycle;
    List<String> updatedIds = Lists.newArrayList();
    for (MainGridViewport viewport : _viewports.values()) {
      CollectionUtils.addIgnoreNull(updatedIds, viewport.updateResults(cache));
    }
    for (DependencyGraphGrid grid : _depGraphs.values()) {
      updatedIds.addAll(grid.updateResults(cycle));
    }
    return updatedIds;
  }

  /**
   * Updates a viewport on the main grid, e.g. in response the the user scrolling the grid.
   *
   * @param viewportId ID of the viewport
   * @param viewportDefinition Definition of the updated viewport
   * @return The viewport's callback ID if it was updated or {@code null} if not
   * @throws DataNotFoundException If no viewport exists with the specified ID
   */
  /* package */ String updateViewport(int viewportId, ViewportDefinition viewportDefinition) {
    return getViewport(viewportId).update(viewportDefinition, _cache);
  }

  // -------- dependency graph grids --------

  /**
   * Opens a depdency graph grid showing the steps used to calculate a cell's value.
   * @param graphId Unique ID of the dependency graph
   * @param gridId ID passed to listeners when the grid's row and column structure changes, this can be any unique value
   * @param row Row index of the cell whose dependency graph is required
   * @param col Column index of the cell whose dependency graph is required
   * @param compiledViewDef Compiled view definition containing the full dependency graph
   * TODO a better way to specify which cell we want - target spec? stable row ID generated on the server?
   * one of these will be needed when dynamic view aggregation is implemented
   */
  /* package */ void openDependencyGraph(int graphId,
                                         String gridId,
                                         int row,
                                         int col,
                                         CompiledViewDefinition compiledViewDef) {
    if (_depGraphs.containsKey(graphId)) {
      throw new IllegalArgumentException("Dependency graph ID " + graphId + " is already in use");
    }
    Pair<String, ValueSpecification> targetForCell = _gridStructure.getTargetForCell(row, col);
    if (targetForCell == null) {
      throw new DataNotFoundException("No dependency graph is available for row " + row + ", col " + col);
    }
    String calcConfigName = targetForCell.getFirst();
    ValueSpecification valueSpec = targetForCell.getSecond();
    DependencyGraphGrid grid =
        DependencyGraphGrid.create(compiledViewDef, valueSpec, calcConfigName, _cycle, gridId, _targetResolver);
    _depGraphs.put(graphId, grid);
  }

  /**
   * Returns an existing dependency graph grid.
   * @param graphId ID of the dependency graph
   * @return The dependency graph grid
   * @throws DataNotFoundException If no dependency graph exists with the specified ID
   */
  private DependencyGraphGrid getDependencyGraph(int graphId) {
    DependencyGraphGrid grid = _depGraphs.get(graphId);
    if (grid == null) {
      throw new DataNotFoundException("No dependency graph found with ID " + graphId + " for " + _gridType + " grid");
    }
    return grid;
  }

  /**
   * Closes an existing dependency graph grid.
   * @param graphId ID of the dependency graph
   * @throws DataNotFoundException If no dependency graph exists with the specified ID
   */
  /* package */ void closeDependencyGraph(int graphId) {
    AnalyticsGrid grid = _depGraphs.remove(graphId);
    if (grid == null) {
      throw new DataNotFoundException("No dependency graph found with ID " + graphId + " for " + _gridType + " grid");
    }
  }

  /**
   * Returns the grid structure for a dependency graph.
   * @param graphId ID of the dependency graph
   * @return The grid structure of the specified dependency graph
   * @throws DataNotFoundException If no dependency graph exists with the specified ID
   */
  /* package */ DependencyGraphGridStructure getGridStructure(int graphId) {
    return getDependencyGraph(graphId).getGridStructure();
  }

  /**
   * Creates a viewport on a dependency graph grid.
   *
   * @param graphId ID of the dependency graph
   * @param viewportId ID of the viewport, can be any unique value
   * @param callbackId ID passed to listeners when the viewport's data changes, can be any unique value
   * @param viewportDefinition Definition of the viewport
   * @return {@code true} if there is data available for the new viewport
   */
  /* package */ boolean createViewport(int graphId, int viewportId, String callbackId, ViewportDefinition viewportDefinition) {
    return getDependencyGraph(graphId).createViewport(viewportId, callbackId, viewportDefinition);
  }

  /**
   * Updates an existing viewport on a dependency graph grid
   *
   * @param graphId ID of the dependency graph
   * @param viewportId ID of the viewport
   * @param viewportDefinition Definition of the viewport
   * @return Version number of the viewport, allows clients to ensure any data they receive for a viewport matches
   * the current viewport state
   * @throws DataNotFoundException If no dependency graph exists with the specified ID
   */
  /* package */ String updateViewport(int graphId, int viewportId, ViewportDefinition viewportDefinition) {
    return getDependencyGraph(graphId).updateViewport(viewportId, viewportDefinition, _cycle);
  }

  /**
   * Deletes an existing viewport on a dependency graph grid.
   * @param graphId ID of the dependency graph
   * @param viewportId ID of the viewport, can be any unique value
   * @throws DataNotFoundException If no dependency graph exists with the specified ID
   */
  /* package */ void deleteViewport(int graphId, int viewportId) {
    getDependencyGraph(graphId).deleteViewport(viewportId);
  }

  /**
   * Returns the data for a viewport on a dependency graph grid.
   * @param graphId ID of the dependency graph
   * @param viewportId ID of the viewport, can be any unique value
   * @return The current data for the viewport
   * @throws DataNotFoundException If no dependency graph exists with the specified ID
   */
  /* package */ ViewportResults getData(int graphId, int viewportId) {
    return getDependencyGraph(graphId).getData(viewportId);
  }

  /**
   * @return The IDs for all depdendency graph grids that are sent to listeners when the grid structure changes
   */
  /* package */ List<String> getDependencyGraphCallbackIds() {
    List<String> gridIds = new ArrayList<String>();
    for (AnalyticsGrid grid : _depGraphs.values()) {
      gridIds.add(grid.getCallbackId());
    }
    return gridIds;
  }

  /**
   * @return The row and column structure of the main grid
   */
  @Override
  public GridStructure getGridStructure() {
    return _gridStructure;
  }

  /**
   * Resolver that doesn't resolve anything, used for grids that will always be empty.
   */
  protected static class DummyTargetResolver implements ComputationTargetResolver {

    @Override
    public ComputationTarget resolve(final ComputationTargetSpecification specification, final VersionCorrection versionCorrection) {
      return null;
    }

    @Override
    public ComputationTargetType simplifyType(final ComputationTargetType type) {
      return type;
    }

    @Override
    public SecuritySource getSecuritySource() {
      return null;
    }

    @Override
    public ComputationTargetSpecificationResolver getSpecificationResolver() {
      throw new UnsupportedOperationException();
    }

    @Override
    public AtVersionCorrection atVersionCorrection(final VersionCorrection versionCorrection) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ChangeManager changeManager() {
      return DummyChangeManager.INSTANCE;
    }

  }
}
