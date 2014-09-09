package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

abstract public class Segment {
  String name;
  Integer length;
  public Segment() {
    super();
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Integer getLength() {
    return length;
  }
  public void setLength(Integer length) {
    this.length = length;
  }
  abstract public void setValues (MultivaluedMap<String, String> m);
  abstract public List<Color> getLeds();
  
  @Override
  public String toString() {
    return String.format("Segment [name=%s, length=%s]", name, length);
  }
}
