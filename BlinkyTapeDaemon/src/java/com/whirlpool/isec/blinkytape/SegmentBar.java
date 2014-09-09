package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.*;

public class SegmentBar extends Segment  {
  Color c;
  Integer v = 0;

  @Override
  public void setValue(String name, Object value) {
    if (name.equals("color")) {
      if (value instanceof Color) {
        c = (Color) value;
      } else {
        throw new IllegalArgumentException("color must be a color");
      }
    } else if (name.equals("value")) {
      if (value instanceof Number) {
        v = ((Number) value).intValue();
      } else {
        throw new IllegalArgumentException("value must be a Number");
      }
    } else {
      throw new IllegalArgumentException("Unknown parameter " + name); 
    }
  }

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      Color c1 = Color.black;
      if (i <= v) c1 = c;
      rv.add(c1);
    }
    return rv;
  }

}
