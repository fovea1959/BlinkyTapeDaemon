package com.whirlpool.isec.blinkytape.config;

import java.util.*;

import com.whirlpool.isec.blinkytape.renderers.AbstractRenderer;
import com.whirlpool.isec.blinkytape.segments.Segment;

public class Config {

  private List<TapeConfig> tapeConfigs = new ArrayList<TapeConfig>();

  private Map<String, Segment> segmentValues = new HashMap<String, Segment>();

  private boolean shutdownRequested = false;

  static private Config instance = new Config();

  static public Config getInstance() {
    return instance;
  }

  private Config() {
  }

  public void addTapeConfig(TapeConfig tapeConfig) {
    tapeConfigs.add(tapeConfig);
  }

  public List<TapeConfig> getTapeConfigs() {
    return tapeConfigs;
  }

  public void postParse() {
    for (TapeConfig tapeconfig : tapeConfigs) {
      for (AbstractRenderer segment : tapeconfig.getSegments()) {
        String name = segment.getName();
        if (name != null) {
          if (segmentValues.get(name) == null) {
            segmentValues.put(segment.getName(), segment.createParametersInstance());
          }
        }
      }
    }
  }

  public Segment getSegmentValue(String s) {
    return segmentValues.get(s);
  }

  @Override
  public String toString() {
    return String.format("Config %s", tapeConfigs);
  }

  public boolean isShutdownRequested() {
    return shutdownRequested;
  }

  public void setShutdownRequested(boolean shutdownRequested) {
    this.shutdownRequested = shutdownRequested;
  }
}
