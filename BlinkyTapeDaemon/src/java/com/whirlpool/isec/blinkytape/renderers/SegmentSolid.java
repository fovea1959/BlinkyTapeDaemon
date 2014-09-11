package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

public class SegmentSolid extends Segment<SegmentSolidParameters> {
  private Color color = Color.WHITE;

  @Override
  SegmentSolidParameters createParametersInstance() {
    SegmentSolidParameters rv = new SegmentSolidParameters(this);
    rv.setColor(this.getColor());
    return rv;
  }

  public Color getColor() {
    return color;
  }
  
  public void setColor(Color color) {
    logger.info ("setting color on {} to {}", this, color);
    this.color = color;
  }

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      rv.add(getParameters().getColor());
    }
    return rv;
  }

}
