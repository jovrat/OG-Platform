/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.server.push.rest;

import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.opengamma.web.server.push.analytics.AnalyticsView;
import com.opengamma.web.server.push.analytics.GridStructure;
import com.opengamma.web.server.push.analytics.ViewportSpecification;

/**
 *
 */
public class MainGridResource extends AbstractGridResource implements DependencyGraphOwnerResource {

  public MainGridResource(AnalyticsView.GridType gridType, AnalyticsView view) {
    super(gridType, view);
  }

  @Override
  public GridStructure getGridStructure() {
    return _view.getGridStructure(_gridType);
  }

  @Override
  public long createViewport(String viewportId, String dataId, ViewportSpecification viewportSpecification) {
    return _view.createViewport(_gridType, viewportId, dataId, viewportSpecification);
  }

  @Override
  public AbstractViewportResource getViewport(String viewportId) {
    return new MainGridViewportResource(_gridType, _view, viewportId);
  }

  @Override
  public Response openDependencyGraph(UriInfo uriInfo, int row, int col) {
    String graphId = Long.toString(s_nextId.getAndIncrement());
    URI graphUri = uriInfo.getAbsolutePathBuilder().path(graphId).build();
    URI gridUri = uriInfo.getAbsolutePathBuilder().path(graphId).path(AbstractGridResource.class, "getGridStructure").build();
    String gridId = gridUri.getPath();
    _view.openDependencyGraph(_gridType, graphId, gridId, row, col);
    return Response.status(Response.Status.CREATED).header(HttpHeaders.LOCATION, graphUri).build();
  }

  @Override
  public AbstractGridResource getDependencyGraph(String graphId) {
    return new DependencyGraphResource(_gridType, _view, graphId);
  }
}
