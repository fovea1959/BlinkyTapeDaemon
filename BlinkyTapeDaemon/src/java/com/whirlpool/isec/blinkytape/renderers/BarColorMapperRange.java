package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;

public class BarColorMapperRange implements Comparable<BarColorMapperRange>{
  private Double upperLimit = null;
  private Color color = Color.black;
  public Double getUpperLimit() {
    return upperLimit;
  }
  public void setUpperLimit(Double limit) {
    this.upperLimit = limit;
  }
  public Color getColor() {
    return color;
  }
  public void setColor(Color color) {
    this.color = color;
  }
  @Override
  public String toString() {
    return String.format("BarColorMapperRange [upperLimit=%s, color=%s]", upperLimit, color);
  }
  @Override
  public int compareTo(BarColorMapperRange o) {
    Double l1 = (upperLimit == null) ? Double.MAX_VALUE : upperLimit;
    Double l2 = (o.upperLimit == null) ? Double.MAX_VALUE : o.upperLimit;
    return l1.compareTo(l2);
  }
}
