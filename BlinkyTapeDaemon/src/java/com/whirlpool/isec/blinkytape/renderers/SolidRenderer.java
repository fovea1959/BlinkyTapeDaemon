package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.segments.SegmentWithColor;

public class SolidRenderer extends AbstractRenderer {
  private Color color = Color.WHITE;

  @Override
  public SegmentWithColor createParametersInstance() {
    SegmentWithColor rv = new SegmentWithColor(getName());
    rv.setColor(this.getColor());
    return rv;
  }

  public Color getColor() {
    return color;
  }
  
  public void setColor(Color color) {
    logger.debug ("setting color on {} to {}", this, color);
    this.color = color;
  }

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    SegmentWithColor value = (SegmentWithColor) Config.getInstance().getSegmentValue(getName());
    for (int i = 0; i < getLength(); i++) {
      Color c = value.getColor();
      // logger.info("adding {} to color for {}", c, this);
      rv.add(c);
    }
    return rv;
  }

  @Override
  public String toString() {
    return String.format("%s [color=%s]", super.toString(), color);
  }

}
