package com.whirlpool.isec.blinkytape.segmentrenderers;

import java.awt.Color;
import java.util.*;

@SuppressWarnings("rawtypes")
public class SegmentOfStatic extends Segment<SegmentParameters> {
  long lastUpdate = 0;
  static private Random random = new Random();
  
  public SegmentOfStatic() {
    super();
    setName(this.toString());
    super.setLength(1);
  }

  @SuppressWarnings("unchecked")
  @Override
  SegmentParameters createParametersInstance() {
    return new SegmentParameters(this);
  }

  @Override
  public void gazeAtMyNavelAmIReallyCurrent() {
    long now = System.currentTimeMillis();
    if (now - lastUpdate > 200) {
      markChanged(now);
    }
  }

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      int b = random.nextInt(16);
      rv.add(new Color(b, b, b));
    }
    return rv;
  }

}
