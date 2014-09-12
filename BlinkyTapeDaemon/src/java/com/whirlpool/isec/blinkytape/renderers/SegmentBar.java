package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.*;

public class SegmentBar extends SegmentSolid {
  @Override
  SegmentBarParameters createParametersInstance() {
    SegmentBarParameters rv = new SegmentBarParameters(this);
    rv.setColor(this.getColor());
    return rv;
  }

  @Override
  public List<Color> getLedsForInformation() {
    List<Color> rv = new ArrayList<Color>(getLength());
    double value = ((SegmentBarParameters) getParameters()).getValue();
    for (int i = 1; i <= getLength(); i++) {
      Color c1 = Color.black;
      if (i < value)
        c1 = getParameters().getColor();
      rv.add(c1);
    }
    return rv;
  }

}
