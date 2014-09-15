package com.whirlpool.isec.blinkytape.config;

import java.util.ArrayList;
import java.util.List;

import com.whirlpool.isec.blinkytape.segmentrenderers.Segment;
import com.whirlpool.isec.blinkytape.segmentrenderers.SegmentParameters;
import com.whirlpool.isec.blinkytape.taperenderers.ITapeRenderer;

public class TapeConfig {
  private String name;

  private List<ITapeRenderer> tapeRenderers = new ArrayList<ITapeRenderer>();

  @SuppressWarnings("rawtypes")
  private List<Segment<? extends SegmentParameters>> segments = new ArrayList<Segment<? extends SegmentParameters>>();

  @SuppressWarnings("rawtypes")
  public List<Segment<? extends SegmentParameters>> getSegments() {
    return segments;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @SuppressWarnings("rawtypes")
  public void addSegment(Segment<? extends SegmentParameters> segment) {
    segments.add(segment);
  }
  
  public void addTapeRenderer(ITapeRenderer tapeRenderer) {
    tapeRenderers.add(tapeRenderer);
  }

  public List<ITapeRenderer> getTapeRenderers() {
    return tapeRenderers;
  }

  @Override
  public String toString() {
    return String.format("TapeConfig [name=%s, segments=%s]", name, segments);
  }

}
