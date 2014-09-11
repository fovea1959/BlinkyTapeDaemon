package com.whirlpool.isec.blinkytape.renderers;
import org.slf4j.*;

abstract public class SegmentParameters {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  Segment segment;

  public SegmentParameters(Segment s) {
    segment = s;
  }
  
  public String getName() {
    return segment.getName();
  }
}
