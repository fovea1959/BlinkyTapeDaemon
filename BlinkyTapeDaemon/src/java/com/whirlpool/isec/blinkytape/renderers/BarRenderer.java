package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.segments.IHasColorProperty;
import com.whirlpool.isec.blinkytape.segments.IHasNumericValueProperty;
import com.whirlpool.isec.blinkytape.segments.Segment;
import com.whirlpool.isec.blinkytape.segments.SegmentWithColorAndNumber;

public class BarRenderer extends SolidRenderer {
  Double min = 0.0;
  Double max = 1.0;
  boolean round = true;

  @Override
  public SegmentWithColorAndNumber createParametersInstance() {
    SegmentWithColorAndNumber rv = new SegmentWithColorAndNumber(this.getName());
    rv.setColor(this.getColor());
    return rv;
  }

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    
    Segment state = Config.getInstance().getSegmentValue(getName());
    
    Double value = null;
    if (state instanceof IHasNumericValueProperty) {
      value = ((IHasNumericValueProperty) state).getValue();
    } else {
      logger.error("state {} doesn't contain a numeric value");
    }
    
    Color stateColor = null;
    if (state instanceof IHasColorProperty) {
      stateColor = ((IHasColorProperty) state).getColor();
    } else {
      logger.error("state {} doesn't contain a color");
    }
    
    value = mapValue(value);
    boolean over = false;
    for (int i = 1; i <= getLength(); i++) {
      Color c1 = Color.black;
      if (i <= value) {
        logger.debug("** {} <= {}", i, value);
        c1 = stateColor;
      } else {
        if (!over) logger.debug("{} !<= {}", i, value);
        over = true;
      }
      rv.add(c1);
    }
    return rv;
  }
  
  double mapValue(double v) {
    double rv = ((v - getMin()) / (getMax() - getMin())) * getLength();
    if (isRound()) rv = Math.round(rv);
    return rv;
  }
  
  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }

  public boolean isRound() {
    return round;
  }

  public void setRound(boolean round) {
    this.round = round;
  }

}
