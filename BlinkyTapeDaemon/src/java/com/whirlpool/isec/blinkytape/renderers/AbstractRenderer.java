package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.util.List;

import org.slf4j.*;

import com.whirlpool.isec.blinkytape.config.Config;
import com.whirlpool.isec.blinkytape.data.Datum;

abstract public class AbstractRenderer {
  private static Logger staticLogger = LoggerFactory.getLogger(AbstractRenderer.class);

  Logger logger;

  String name;

  private Integer length;
  
  private long lastChangedAt = 0;

  public AbstractRenderer() {
    super();
    logger = staticLogger;
  }

  abstract public Datum createDatumInstance();

  final public String getName() {
    return name;
  }

  final public void setName(String name) {
    logger = LoggerFactory.getLogger(AbstractRenderer.class.getName() + "." + name);
    this.name = name;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }
  
  public void postConfig() {
  }

  abstract public List<Color> getLeds();
  
  public void gazeAtMyNavelAmIReallyCurrent() {
  }
  
  @Override
  public String toString() {
    return String.format("%s [name=%s, length=%s]", super.toString(), name, length);
  }
  
  public void markChanged() {
    markChanged(System.currentTimeMillis());
  }
  
  public void markChanged(long when) {
    lastChangedAt = when;
  }

  public long getLastChangedAt() {
    long rv = Config.getInstance().getDatum(getName()).getLastChangedAt();
    markChanged();
    if (lastChangedAt > rv) rv = lastChangedAt;
    return lastChangedAt;
  }

}
