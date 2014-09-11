package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

public class SegmentBar extends Segment<SegmentBarParameters> {
  private Color color = Color.WHITE;

  @Override
  SegmentBarParameters createParametersInstance() {
    SegmentBarParameters rv = new SegmentBarParameters(this);
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
    int value = getParameters().getValue();
    for (int i = 0; i < getLength(); i++) {
      Color c1 = Color.black;
      if (i < value)
        c1 = getParameters().getColor();
      rv.add(c1);
    }
    return rv;
  }

}
