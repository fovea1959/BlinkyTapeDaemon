package com.whirlpool.isec.blinkytape.config;

import java.util.*;

import com.whirlpool.isec.blinkytape.data.Datum;
import com.whirlpool.isec.blinkytape.renderers.AbstractRenderer;

public class Config {

  private List<TapeConfig> tapeConfigs = new ArrayList<TapeConfig>();

  private Map<String, Datum> data = new HashMap<String, Datum>();

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

  public void postConfig() {
    for (TapeConfig tapeconfig : tapeConfigs) {
      for (AbstractRenderer renderer : tapeconfig.getRenderers()) {
        String name = renderer.getName();
        if (name != null) {
          if (data.get(name) == null) {
            data.put(renderer.getName(), renderer.createDatumInstance());
          }
        }
        renderer.postConfig();
      }
    }
  }

  public Datum getDatum(String s) {
    return data.get(s);
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
