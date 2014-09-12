package com.whirlpool.isec.blinkytape.renderers;

import java.util.Collections;
import java.util.List;

public class KeeptheStripAlive extends Segment {
  public KeeptheStripAlive() {
    super();
    setName("wussyWussyWussy");
  }
  
  
  @Override
  public void gazeAtMyNavelAmIReallyCurrent() {
    long t1 = System.currentTimeMillis();
    if ((t1 - t0) > 2000) {
      logger.info("ok, I am old");
      blinkyTapeVersionIsNowObsoleteWarningWillRobinsonWarning();
      t0 = t1;
    }
  }

  long t0 = System.currentTimeMillis();

  @Override
  SegmentParameters createParametersInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List getLedsForInformation() {
    // TODO Auto-generated method stub
    return Collections.EMPTY_LIST;
  }

}
