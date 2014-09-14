package com.whirlpool.isec.blinkytape.renderers;
import org.slf4j.*;

@SuppressWarnings("rawtypes")
abstract public class SegmentParameters<S extends Segment> {
  static Logger staticLogger = LoggerFactory.getLogger(SegmentParameters.class);
  Logger logger = null;
  
  S segment;
  
  public SegmentParameters (S s) {
    segment = s;
    logger = LoggerFactory.getLogger(SegmentParameters.class.getName() + "." + s.getName());
  }
  
  public String getName() {
    return segment.getName();
  }
}
