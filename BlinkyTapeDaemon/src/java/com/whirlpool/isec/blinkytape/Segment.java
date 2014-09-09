package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.List;

abstract public class Segment {
  public Segment() {
    super();
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Integer getStart() {
    return start;
  }
  public void setStart(Integer start) {
    this.start = start;
  }
  public Integer getLength() {
    return length;
  }
  public void setLength(Integer length) {
    this.length = length;
  }
  abstract public void setValue (String name, Object value);
  abstract public List<Color> getLeds();
  String name;
  Integer start, length;
  @Override
  public String toString() {
    return String.format("Segment [name=%s, start=%s, length=%s]", name, start, length);
  }
}
