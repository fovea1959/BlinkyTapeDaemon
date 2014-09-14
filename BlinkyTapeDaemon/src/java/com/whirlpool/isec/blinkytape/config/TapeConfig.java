package com.whirlpool.isec.blinkytape.config;

import java.util.ArrayList;
import java.util.List;

import com.whirlpool.isec.blinkytape.renderers.Segment;
import com.whirlpool.isec.blinkytape.renderers.SegmentParameters;

public class TapeConfig {
  private String name;

  private boolean reverse;

  @SuppressWarnings("rawtypes")
  private List<Segment<? extends SegmentParameters>> segments = new ArrayList<Segment<? extends SegmentParameters>>();

  @SuppressWarnings("rawtypes")
  public List<Segment<? extends SegmentParameters>> getSegments() {
    return segments;
  }

  public String getName() {
    return name;
  }

  public boolean isReverse() {
    return reverse;
  }

  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }

  public void setName(String name) {
    this.name = name;
  }

  @SuppressWarnings("rawtypes")
  public void addSegment(Segment<? extends SegmentParameters> segment) {
    segments.add(segment);
  }

  @Override
  public String toString() {
    return String.format("TapeConfig [name=%s, reverse=%s, segments=%s]", name, reverse,
        segments);
  }

}
