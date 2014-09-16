package com.whirlpool.isec.blinkytape.data;

import java.awt.Color;

public class DatumWithColor extends Datum implements IHasColorProperty {
  public DatumWithColor(String name) {
    super(name);
  }
  Color color = Color.WHITE;

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    logger.debug ("setting color on {} to {}", this, color);
    this.color = color;
  }
  
  @Override
  public String toString() {
    return String.format("%s [name=%s, color=%s]", super.toString(), getName(), color);
  }

}