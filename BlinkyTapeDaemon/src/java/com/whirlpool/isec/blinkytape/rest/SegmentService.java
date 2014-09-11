package com.whirlpool.isec.blinkytape.rest;

/**
 * @author Crunchify.com
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.EmbeddedServer;
import com.whirlpool.isec.blinkytape.Util;
import com.whirlpool.isec.blinkytape.renderers.Segment;

@Path("/segment")
public class SegmentService {
  static Logger logger = LoggerFactory.getLogger(SegmentService.class);

  @Path("{s}")
  @GET
  @Produces("application/xml")
  public String setColor(@PathParam("s") String s, @Context UriInfo ui) {
    Util.setupConverters();
    MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
    logger.info("s={}, qp={}", s, queryParams);
    Segment<?> segment = EmbeddedServer.config.getSegment(s);
    if (segment == null) throw new WebApplicationException("Cannot find '" + s + "'", 400);
    segment.setValues(queryParams);
    return "<segment>" + segment + "</segment>";
  }
}