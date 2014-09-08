package com.whirlpool.isec.blinkytape.rest;

/**
 * @author Crunchify.com
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.EmbeddedServer;

@Path("/b")
public class BService {
  static Logger logger = LoggerFactory.getLogger(BService.class);

  @Path("{s}")
  @GET
  @Produces("application/xml")
  public String setColor(@PathParam("s") String s,
      @QueryParam("color") ColorParam colorParam,
      @QueryParam("delay") Integer delay) {
    logger.info("s={}, color={}, delay={}", s, colorParam, delay);
    if (colorParam != null) {
      EmbeddedServer.cylon.setColor(colorParam);
      EmbeddedServer.doubleBar.setColor(colorParam);
    }
    if (delay != null) {
      EmbeddedServer.cylon.setDelay(delay);
      EmbeddedServer.doubleBar.setDelay(delay);
    }
    return "<bservice>" + EmbeddedServer.cylon + "</bservice>";
  }
}