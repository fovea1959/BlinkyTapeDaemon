package com.whirlpool.isec.blinkytape.config;

import java.util.ArrayList;
import java.util.List;

import com.whirlpool.isec.blinkytape.renderers.AbstractRenderer;
import com.whirlpool.isec.blinkytape.tapes.ITape;

public class TapeConfig {
  private String name;

  private List<ITape> tapes = new ArrayList<ITape>();

  private List<AbstractRenderer> renderers = new ArrayList<AbstractRenderer>();

  public List<AbstractRenderer> getRenderers() {
    return renderers;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addRenderer(AbstractRenderer renderer) {
    renderers.add(renderer);
  }
  
  public void addTape(ITape iTape) {
    tapes.add(iTape);
  }

  public void removeTape(ITape iTape) {
    tapes.remove(iTape);
  }

  public List<ITape> getTapes() {
    return tapes;
  }

  @Override
  public String toString() {
    return String.format("TapeConfig [name=%s, renderers=%s]", name, renderers);
  }

}
