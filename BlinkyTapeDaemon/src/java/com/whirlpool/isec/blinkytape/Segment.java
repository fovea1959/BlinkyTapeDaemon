package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.*;

abstract public class Segment {
  private static Logger staticLogger = LoggerFactory.getLogger(Segment.class);

  Logger logger;

  String name;

  Integer length;

  public Segment() {
    super();
    logger = staticLogger;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    logger = LoggerFactory.getLogger(Segment.class.getName() + "." + name);
    this.name = name;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public void setValues(MultivaluedMap<String, String> m) {
    // ConvertUtils.register(new ColorConverter(), Color.class);
    logger.warn("Converter = {}", BeanUtilsBean.getInstance());
    for (String name : m.keySet()) {
      String v = m.getFirst(name);
      setValue(name, v);
    }
  }

  public void setValue(String name, String v) {
    try {
      BeanUtils.setProperty(this, name, v);
    } catch (IllegalAccessException | InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  abstract public List<Color> getLeds();

  @Override
  public String toString() {
    return String.format("Segment [name=%s, length=%s]", name, length);
  }
}
