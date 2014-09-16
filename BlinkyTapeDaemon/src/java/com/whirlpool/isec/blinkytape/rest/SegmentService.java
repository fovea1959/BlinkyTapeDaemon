package com.whirlpool.isec.blinkytape.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.Util;
import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.rest.NoRepeater.NoRepeaterResult;
import com.whirlpool.isec.blinkytape.segments.Segment;

@Path("/segment/{s}")
public class SegmentService {
  static Logger logger = LoggerFactory.getLogger(SegmentService.class);
  
  static Config config = Config.getInstance();
  
  static NoRepeater noRepeater = new NoRepeater(5 * 60 * 1000, "Unable to find segment %s");

  @GET
  @Produces("application/xml")
  public String setSegmentGet(@PathParam("s") String s, @Context UriInfo ui, @Context HttpServletRequest httpRequest) {
    MultivaluedMap<String, String> m = ui.getQueryParameters();
    return setSegment(s, m, httpRequest);
  }
  
  @POST
  @Consumes("application/x-www-form-urlencoded")
  @Produces("application/xml")
  public String setSegmentPost(@PathParam("s") String s, MultivaluedMap<String, String> m, @Context HttpServletRequest httpRequest) {
    return setSegment(s, m, httpRequest);
  }
  
  public String setSegment (String s, MultivaluedMap<String, String> m, HttpServletRequest h) {
    Util.setupConverters();
    String remoteHost = h.getRemoteHost();
    logger.debug("from={}, s={}, qp={}", s, m);
    Segment segmentValue = Config.getInstance().getSegmentValue(s);
    if (segmentValue == null) {
      // logger.error("Test: unable to find segment {}", s);
      NoRepeaterResult r = noRepeater.somethingHappened(s + "@" + remoteHost);
      if (r != null) {
        logger.error(r.message());
      }
      throw new WebApplicationException("Cannot find '" + s + "'", 400);
    }
    segmentValue.setValues(m);
    return "<segment>" + segmentValue.toString() + "</segment>";
  }
}