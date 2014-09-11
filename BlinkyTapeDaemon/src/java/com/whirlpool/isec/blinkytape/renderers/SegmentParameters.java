package com.whirlpool.isec.blinkytape.renderers;
import org.slf4j.*;

@SuppressWarnings("rawtypes")
abstract public class SegmentParameters<S extends Segment> {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  S segment;

  public SegmentParameters (S s) {
    segment = s;
  }
  
  public String getName() {
    return segment.getName();
  }
}
