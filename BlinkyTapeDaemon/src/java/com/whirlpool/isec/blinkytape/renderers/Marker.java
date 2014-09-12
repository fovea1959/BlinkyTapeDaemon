package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.List;

public class Marker extends SegmentSolid {
  boolean wasOn = false;

  public Marker() {
    super();
    setName(this.toString());
    setLength(1);
  }

  @Override
  SegmentSolidParameters createParametersInstance() {
    // TODO Auto-generated method stub
    return new MarkerParameters(this);
  }

  @Override
  public void gazeAtMyNavelAmIReallyCurrent() {
    boolean shouldBeOn = wellAmILit();
    if (shouldBeOn != wasOn)
      blinkyTapeVersionIsNowObsoleteWarningWillRobinsonWarning();
    wasOn = shouldBeOn;
  }

  boolean wellAmILit() {
    long t = System.currentTimeMillis();
    return (t % 5000 < 100);
  }

  @Override
  public List<Color> getLedsForInformation() {
    List<Color> rv = super.getLedsForInformation();
    if (!wellAmILit()) {
      for (int i = 0; i < rv.size(); i++) 
        rv.set(i,  Color.black);
    }
    return rv;
  }

}
