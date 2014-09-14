package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public class KeeptheStripAlive extends SegmentSolid {
  public KeeptheStripAlive() {
    super();
    setName("KeepTheStripAlive");
  }
  
  
  @Override
  public void gazeAtMyNavelAmIReallyCurrent() {
    long t1 = System.currentTimeMillis();
    if ((t1 - t0) > 2000) {
      blinkyTapeVersionIsNowObsoleteWarningWillRobinsonWarning();
      t0 = t1;
    }
  }

  long t0 = System.currentTimeMillis();

  @Override
  SegmentSolidParameters createParametersInstance() {
    return null;
  }

  @Override
  public List<Color> getLedsForInformation() {
    return Collections.emptyList();
  }

}
