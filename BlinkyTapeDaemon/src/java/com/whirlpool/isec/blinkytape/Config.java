package com.whirlpool.isec.blinkytape;

import java.util.*;

public class Config {
  @Override
  public String toString() {
    return String.format("Config [segments=%s]", segments);
  }

  private List<Segment> segments = new ArrayList<Segment>();

  private Map<String, Segment> segmentMap = new HashMap<String, Segment>();

  public void addSegment(Segment segment) {
    segments.add(segment);
    segmentMap.put(segment.getName(), segment);
  }

  public Segment getSegment(String name) {
    return segmentMap.get(name);
  }

  public List<Segment> getSegments() {
    return segments;
  }

}
