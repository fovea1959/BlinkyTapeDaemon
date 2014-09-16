package com.whirlpool.isec.blinkytape.config;

import java.util.ArrayList;
import java.util.List;

import com.whirlpool.isec.blinkytape.renderers.AbstractRenderer;
import com.whirlpool.isec.blinkytape.tapes.ITape;

public class TapeConfig {
  private String name;

  private List<ITape> tapeRenderers = new ArrayList<ITape>();

  private List<AbstractRenderer> segments = new ArrayList<AbstractRenderer>();

  public List<AbstractRenderer> getSegments() {
    return segments;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addSegment(AbstractRenderer segment) {
    segments.add(segment);
  }
  
  public void addTapeRenderer(ITape tapeRenderer) {
    tapeRenderers.add(tapeRenderer);
  }

  public List<ITape> getTapeRenderers() {
    return tapeRenderers;
  }

  @Override
  public String toString() {
    return String.format("TapeConfig [name=%s, segments=%s]", name, segments);
  }

}
