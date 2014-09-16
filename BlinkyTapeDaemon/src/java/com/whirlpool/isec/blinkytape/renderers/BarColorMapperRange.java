package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;

public class BarColorMapperRange implements Comparable<BarColorMapperRange>{
  private Double limit = 0.0;
  private Color color = Color.black;
  public Double getLimit() {
    return limit;
  }
  public void setLimit(Double limit) {
    this.limit = limit;
  }
  public Color getColor() {
    return color;
  }
  public void setColor(Color color) {
    this.color = color;
  }
  @Override
  public String toString() {
    return String.format("BarColorMapperRange [limit=%s, color=%s]", limit, color);
  }
  @Override
  public int compareTo(BarColorMapperRange o) {
    return limit.compareTo(o.limit);
  }
}
