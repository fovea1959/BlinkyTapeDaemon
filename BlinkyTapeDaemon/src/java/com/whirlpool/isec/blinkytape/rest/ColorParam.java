package com.whirlpool.isec.blinkytape.rest;

import java.awt.Color;
import java.lang.reflect.Field;

import javax.ws.rs.WebApplicationException;

import org.slf4j.*;

@SuppressWarnings("serial")
public class ColorParam extends Color {
  static Logger logger = LoggerFactory.getLogger(ColorParam.class);
  public ColorParam(String s) {
    super(getRGB(s));
  }

  private static int getRGB(String s) {
    logger.info("got color string '{}'", s);
    if (s.charAt(0) == '#') {
      try {
        Color c = Color.decode("0x" + s.substring(1));
        return c.getRGB();
      } catch (NumberFormatException e) {
        WebApplicationException we =  new WebApplicationException("Invalid color " +
            "code = '" + s + "'", e, 400);
        logger.error("", we);
        throw we;
      }
    } else {
      try {
        Field f = Color.class.getField(s);
        return ((Color) f.get(null)).getRGB();
      } catch (Exception e) {
        WebApplicationException we =  new WebApplicationException("Invalid color " +
            "code = '" + s + "'", e, 400);
        logger.error("", we);
        throw we;
      }
    }
  }
}