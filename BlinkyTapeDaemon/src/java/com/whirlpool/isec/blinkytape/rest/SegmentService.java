package com.whirlpool.isec.blinkytape.rest;

/**
 * @author Crunchify.com
 */

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.Util;
import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.segmentrenderers.Segment;

@Path("/segment")
public class SegmentService {
  static Logger logger = LoggerFactory.getLogger(SegmentService.class);
  
  static Config config = Config.getInstance();

  @Path("{s}")
  @GET
  @Produces("application/xml")
  public String setSegmentGet(@PathParam("s") String s, @Context UriInfo ui) {
    MultivaluedMap<String, String> m = ui.getQueryParameters();
    return setSegment(s, m);
  }
  
  @Path("{s}")
  @POST
  @Consumes("application/x-www-form-urlencoded")
  @Produces("application/xml")
  public String setSegmentPost(@PathParam("s") String s, MultivaluedMap<String, String> m) {
    return setSegment(s, m);
  }
  
  public String setSegment (String s, MultivaluedMap<String, String> m) {
    Util.setupConverters();
    logger.info("s={}, qp={}", s, m);
    Segment<?> segment = Config.getInstance().getNamedSegment(s);
    if (segment == null) {
      logger.error("Cannot find segment {}", s);
      throw new WebApplicationException("Cannot find '" + s + "'", 400);
    }
    segment.setValues(m);
    return "<segment>" + segment.getParameters() + "</segment>";
  }
}