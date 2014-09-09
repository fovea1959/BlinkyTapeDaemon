package com.whirlpool.isec.blinkytape.rest;

/**
 * @author Crunchify.com
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.EmbeddedServer;
import com.whirlpool.isec.blinkytape.Segment;

@Path("/segment")
public class BService {
  static Logger logger = LoggerFactory.getLogger(BService.class);

  @Path("{s}")
  @GET
  @Produces("application/xml")
  public String setColor(@PathParam("s") String s,
      @QueryParam("color") ColorParam colorParam,
      @QueryParam("value") Integer value) {
    logger.info("s={}, color={}, value={}", s, colorParam, value);
    Segment segment = EmbeddedServer.config.getSegment(s);
    if (segment == null) throw new WebApplicationException("Cannot find '" + s + "'", 400);
    if (colorParam != null) {
      segment.setValue("color",  colorParam);
    }
    if (value != null) {
      segment.setValue("value",  value);
    }
    return "<bservice>" + segment + "</bservice>";
  }
}