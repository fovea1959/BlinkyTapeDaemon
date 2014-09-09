package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

import com.whirlpool.isec.blinkytape.rest.ColorParam;

public class SegmentBar extends Segment {
  Color c;

  Integer value = 0;

  @Override
  public void setValues(MultivaluedMap<String, String> m) {
    for (String name : m.keySet()) {
      String v = m.getFirst(name);
      if (name.equals("color")) {
        c = new ColorParam(v);
      } else if (name.equals("value")) {
        value = (new Double(v)).intValue();
      } else {
        throw new WebApplicationException("Unknown attribute = '" + name + "'", 404);
      }
    }
  }

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      Color c1 = Color.black;
      if (i <      value)
        c1 = c;
      rv.add(c1);
    }
    return rv;
  }

}
