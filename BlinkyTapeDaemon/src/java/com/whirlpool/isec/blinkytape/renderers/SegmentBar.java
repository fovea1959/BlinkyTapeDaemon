package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

public class SegmentBar extends Segment<SegmentBar.Parameters> {
  private Color color = Color.WHITE;

  @Override
  Parameters createParametersInstance() {
    Parameters rv = new Parameters();
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

  public class Parameters extends SegmentParameters {
    Color color = Color.WHITE;

    Integer value = 0;

    public Parameters() {
      super();
    }

    public Color getColor() {
      return color;
    }

    public void setColor(Color color) {
      logger.info ("setting color on {} to {}", this, color);
      this.color = color;
    }

    public Integer getValue() {
      return value;
    }

    public void setValue(Integer value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.format("%s [color=%s, value=%s]", super.toString(), color, value);
    }

  }

}
