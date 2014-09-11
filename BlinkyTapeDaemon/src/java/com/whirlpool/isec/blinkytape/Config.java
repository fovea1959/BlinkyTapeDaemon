package com.whirlpool.isec.blinkytape;

import java.util.*;

import com.whirlpool.isec.blinkytape.renderers.Segment;
import com.whirlpool.isec.blinkytape.renderers.SegmentParameters;

public class Config {
  private String name;

  private List<Segment<? extends SegmentParameters>> segments = new ArrayList<Segment<? extends SegmentParameters>>();

  private Map<String, Segment<? extends SegmentParameters>> segmentMap = new HashMap<String, Segment<? extends SegmentParameters>>();

  public void addSegment(Segment<? extends SegmentParameters> segment) {
    segments.add(segment);
    segmentMap.put(segment.getName(), segment);
  }

  public Segment<? extends SegmentParameters> getSegment(String name) {
    return segmentMap.get(name);
  }

  public List<Segment<? extends SegmentParameters>> getSegments() {
    return segments;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return String.format("Config [name=%s,segments=%s]", name, segments);
  }
  
}
