package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;

public class MarkerParameters extends SegmentSolidParameters {
    public MarkerParameters(SegmentSolid segmentSolid) {
      super (segmentSolid);
      setColor(segmentSolid.getColor());
    }

    @Override
    public void setColor(Color color) {
      logger.info("**MARKER** setting color {}", color);
      super.setColor(color);
    }
  }