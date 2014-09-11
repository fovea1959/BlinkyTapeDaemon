package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.*;

public class SegmentBar extends Segment {
  Color color = Color.WHITE;

  Integer value = 0;

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      Color c1 = Color.black;
      if (i < value)
        c1 = color;
      rv.add(c1);
    }
    return rv;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

}
