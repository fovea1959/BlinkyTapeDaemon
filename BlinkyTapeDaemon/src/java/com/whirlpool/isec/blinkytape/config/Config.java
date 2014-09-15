package com.whirlpool.isec.blinkytape.config;

import java.util.*;

import com.whirlpool.isec.blinkytape.renderers.Segment;

public class Config {

  static private Config instance = new Config();

  static public Config getInstance() {
    return instance;
  }

  private Config() {
  }

  private List<TapeConfig> tapeConfigs = new ArrayList<TapeConfig>();

  @SuppressWarnings("rawtypes")
  private Map<String, Segment> namedSegments = new HashMap<String, Segment>();

  public void addTapeConfig(TapeConfig tapeConfig) {
    tapeConfigs.add(tapeConfig);
  }

  public List<TapeConfig> getTapeConfigs() {
    return tapeConfigs;
  }

  public void postParse() {
    for (TapeConfig tapeconfig : tapeConfigs) {
      for (@SuppressWarnings("rawtypes")
      Segment segment : tapeconfig.getSegments()) {
        if (segment.getName() != null)
          namedSegments.put(segment.getName(), segment);
      }
    }

  }

  @SuppressWarnings("rawtypes")
  public Segment getNamedSegment(String s) {
    return namedSegments.get(s);
  }

  @Override
  public String toString() {
    return String.format("Config %s", tapeConfigs);
  }
}
