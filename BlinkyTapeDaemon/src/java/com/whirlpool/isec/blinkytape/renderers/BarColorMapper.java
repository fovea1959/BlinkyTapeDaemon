package com.whirlpool.isec.blinkytape.renderers;

import org.slf4j.*;

import java.awt.Color;
import java.util.*;

public class BarColorMapper {
  Logger logger = LoggerFactory.getLogger(getClass());
  private List<BarColorMapperRange> ranges = new ArrayList<BarColorMapperRange>();
  private boolean zebra = false;
  
  boolean dirty = false;
  public void addRange (BarColorMapperRange range) {
    logger.debug("adding {}", range);
    ranges.add(range);
    dirty = true;
  }
  
  public List<BarColorMapperRange> getRanges() {
    if (dirty) {
      Collections.sort(ranges);
      dirty = false;
    }
    return ranges;
  }
  
  public Color getColorForValue (Double d) {
    for (BarColorMapperRange r : getRanges()) {
      logger.debug("comparing {} to {}", d, r);
      if (r.getUpperLimit() == null || d <= r.getUpperLimit()) return r.getColor();
    }
    return null;
  }

  public boolean isZebra() {
    return zebra;
  }

  public void setZebra(boolean zebra) {
    this.zebra = zebra;
  }
}
