package com.whirlpool.isec.blinkytape.rest;

import java.awt.Color;
import java.lang.reflect.Field;

import javax.ws.rs.WebApplicationException;

@SuppressWarnings("serial")
public class ColorParam extends Color {
  public ColorParam(String s) {
    super(getRGB(s));
    System.out.println(s);
  }

  private static int getRGB(String s) {
    if (s.charAt(0) == '#') {
      try {
        Color c = Color.decode("0x" + s.substring(1));
        return c.getRGB();
      } catch (NumberFormatException e) {
        throw new WebApplicationException(400);
      }
    } else {
      try {
        Field f = Color.class.getField(s);
        return ((Color) f.get(null)).getRGB();
      } catch (Exception e) {
        throw new WebApplicationException(400);
      }
    }
  }
}