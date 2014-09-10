package com.whirlpool.isec.blinkytape;

import java.util.*;

public class Config {
  private String name;

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
