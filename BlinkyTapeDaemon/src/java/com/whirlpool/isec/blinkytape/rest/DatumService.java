package com.whirlpool.isec.blinkytape.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.NoRepeater;
import com.whirlpool.isec.blinkytape.Util;
import com.whirlpool.isec.blinkytape.NoRepeater.NoRepeaterResult;
import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.data.Datum;

@Path("/datum/{s}")
public class DatumService {
  static Logger logger = LoggerFactory.getLogger(DatumService.class);
  
  static Config config = Config.getInstance();
  
  static NoRepeater noRepeater = new NoRepeater(5 * 60 * 1000, "Unable to find datum %s");

  @GET
  @Produces("application/xml")
  public String setDatum(@PathParam("s") String s, @Context UriInfo ui, @Context HttpServletRequest httpRequest) {
    MultivaluedMap<String, String> m = ui.getQueryParameters();
    return setDatumDo(s, m, httpRequest);
  }
  
  @POST
  @Consumes("application/x-www-form-urlencoded")
  @Produces("application/xml")
  public String setDatum(@PathParam("s") String s, MultivaluedMap<String, String> m, @Context HttpServletRequest httpRequest) {
    return setDatumDo(s, m, httpRequest);
  }
  
  public String setDatumDo (String s, MultivaluedMap<String, String> m, HttpServletRequest h) {
    Util.setupConverters();
    String remoteHost = h.getRemoteHost();
    logger.debug("from={}, s={}, qp={}", s, m);
    Datum datum = Config.getInstance().getDatum(s);
    if (datum == null) {
      // logger.error("Test: unable to find datum {}", s);
      NoRepeaterResult r = noRepeater.somethingHappened(s + "@" + remoteHost);
      if (r != null) {
        logger.error(r.message());
      }
      throw new WebApplicationException("Cannot find '" + s + "'", 400);
    }
    datum.setValues(m);
    return "<datum>" + datum.toString() + "</datum>";
  }
}