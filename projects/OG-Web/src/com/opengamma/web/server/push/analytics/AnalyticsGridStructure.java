/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.server.push.analytics;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.opengamma.core.position.PortfolioNode;
import com.opengamma.core.position.Position;
import com.opengamma.core.position.impl.PortfolioMapper;
import com.opengamma.core.position.impl.PortfolioMapperFunction;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.engine.view.compilation.CompiledViewCalculationConfiguration;
import com.opengamma.engine.view.compilation.CompiledViewDefinition;
import com.opengamma.util.ArgumentChecker;

/**
 *
 */
public class AnalyticsGridStructure {

  private final AnalyticsNode _root;
  private final AnalyticsColumns _columns;
  private final List<Row> _rows;
  // TODO persistent row IDs that can be tracked when the structure changes (for dynamic reaggregation)

  private AnalyticsGridStructure(AnalyticsNode root, AnalyticsColumns columns, List<Row> rows) {
    ArgumentChecker.notNull(root, "root");
    ArgumentChecker.notNull(columns, "columns");
    ArgumentChecker.notNull(rows, "rows");
    _root = root;
    _columns = columns;
    _rows = rows;
  }

  public static AnalyticsGridStructure empty() {
    return new AnalyticsGridStructure(AnalyticsNode.emptyRoot(), AnalyticsColumns.empty(), Collections.<Row>emptyList());
  }

  public static AnalyticsGridStructure portoflio(CompiledViewDefinition compiledViewDef) {
    return new AnalyticsGridStructure(AnalyticsNode.portoflioRoot(compiledViewDef),
                                      AnalyticsColumns.portfolio(compiledViewDef),
                                      portfolioRowsForViewDefinition(compiledViewDef));
  }

  public static AnalyticsGridStructure primitives(CompiledViewDefinition compiledViewDef) {
    return new AnalyticsGridStructure(AnalyticsNode.primitivesRoot(compiledViewDef),
                                      AnalyticsColumns.primitives(compiledViewDef),
                                      primitivesRowsForViewDefinition(compiledViewDef));
  }

  public static AnalyticsGridStructure depdencyGraph(CompiledViewDefinition compiledViewDef) {
    return new AnalyticsGridStructure(AnalyticsNode.portoflioRoot(compiledViewDef),
                                      AnalyticsColumns.dependencyGraph(/*compiledViewDef*/),
                                      /*rowsForViewDefinition(compiledViewDef)*/null/* TODO what here? */);
  }

  private static List<Row> portfolioRowsForViewDefinition(CompiledViewDefinition viewDef) {
    PortfolioMapperFunction<Row> targetFn = new PortfolioMapperFunction<Row>() {
      @Override
      public Row apply(PortfolioNode node) {
        ComputationTargetSpecification target =
            new ComputationTargetSpecification(ComputationTargetType.PORTFOLIO_NODE, node.getUniqueId());
        return new Row(target, node.getName());
      }

      @Override
      public Row apply(Position position) {
        ComputationTargetSpecification target =
            new ComputationTargetSpecification(ComputationTargetType.POSITION, position.getUniqueId());
        // TODO will the security be resolved?
        return new Row(target, position.getSecurity().getName());
      }
    };
    return PortfolioMapper.map(viewDef.getPortfolio().getRootNode(), targetFn);
  }

  private static List<Row> primitivesRowsForViewDefinition(CompiledViewDefinition compiledViewDef) {
    List<Row> rows = Lists.newArrayList();
    for (CompiledViewCalculationConfiguration compiledCalcConfig : compiledViewDef.getCompiledCalculationConfigurations()) {
      for (ValueSpecification valueSpec : compiledCalcConfig.getTerminalOutputSpecifications().keySet()) {
        ComputationTargetSpecification targetSpec = valueSpec.getTargetSpecification();
        if (targetSpec.getType() == ComputationTargetType.PRIMITIVE) {
          // TODO is the row name right?
          rows.add(new Row(targetSpec, valueSpec.getTargetSpecification().getIdentifier().toString()));
        }
      }
    }
    return rows;
  }

  public AnalyticsNode getRoot() {
    return _root;
  }

  public AnalyticsColumns getColumns() {
    return _columns;
  }

  public Row getRowAtIndex(int rowIndex) {
    return _rows.get(rowIndex);
  }

  public int getColumnIndexForRequirement(String calcConfigName, ValueRequirement requirement) {
    return _columns.getColumnIndexForRequirement(calcConfigName, requirement);
  }

  public static class Row {

    private final ComputationTargetSpecification _target;
    private final String _name;

    private Row(ComputationTargetSpecification target, String name) {
      ArgumentChecker.notNull(target, "target");
      ArgumentChecker.notNull(name, "name");
      _target = target;
      _name = name;
    }

    public ComputationTargetSpecification getTarget() {
      return _target;
    }

    public String getName() {
      return _name;
    }

    @Override
    public String toString() {
      return "Row [_target=" + _target + ", _name='" + _name + '\'' + "]";
    }
  }
}
