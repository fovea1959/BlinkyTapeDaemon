package com.whirlpool.isec.blinkytape.renderers;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.*;

@SuppressWarnings("rawtypes")
abstract public class Segment<P extends SegmentParameters> {
  private static Logger staticLogger = LoggerFactory.getLogger(Segment.class);

  Logger logger;

  String name;

  Integer length;

  private P parameters;

  public Segment() {
    super();
    logger = staticLogger;
  }

  public P getParameters() {
    if (parameters == null) {
      parameters = createParametersInstance();
    }
    return parameters;
  }

  abstract P createParametersInstance();

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
    logger.warn("Before = {}", getParameters().toString());
    for (String name : m.keySet()) {
      String v = m.getFirst(name);
      setValue(name, v);
    }
    logger.warn("After = {}", getParameters().toString());
  }

  public void setValue(String name, String v) {
    try {
      logger.warn("Before 1 {} = {}", name, getParameters().toString());
      BeanUtils.setProperty(getParameters(), name, v);
      logger.warn("After 1 = {}", getParameters().toString());
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
