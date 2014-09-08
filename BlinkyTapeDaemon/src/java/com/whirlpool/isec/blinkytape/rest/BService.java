package com.whirlpool.isec.blinkytape.rest;

/**
 * @author Crunchify.com
 */

import java.awt.Color;

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
      @QueryParam("color") ColorParam colorParam) {
    logger.info("s={}, color={}", s, colorParam);
    Color c, c0;
    c = c0 = colorParam;
    if (c == null)
      c = Color.DARK_GRAY;
    EmbeddedServer.cylon.setColor(c);
    return "<bservice>" + c0 + "</bservice>";
  }
}