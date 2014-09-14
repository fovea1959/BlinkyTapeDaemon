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

  private Integer length;
  
  boolean blinkyTapeVersionCurrent = true;

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

  final public String getName() {
    return name;
  }

  final public void setName(String name) {
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
    logger.info("Converter = {}", BeanUtilsBean.getInstance());
    logger.debug("incoming = {}", m.toString());
    logger.debug("before = {}", getParameters().toString());
    for (String name : m.keySet()) {
      String v = m.getFirst(name);
      setValue(name, v);
    }
    logger.debug("after  = {}", getParameters().toString());
    // a fair bet here
    blinkyTapeVersionIsNowObsoleteWarningWillRobinsonWarning();
  }

  public void setValue(String name, String v) {
    logger.debug("setting {} to {}", name, v);
    try {
      // logger.warn("Before 1 {} = {}", name, getParameters().toString());
      BeanUtils.setProperty(getParameters(), name, v);
      // logger.warn("After 1 = {}", getParameters().toString());
    } catch (IllegalAccessException | InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      logger.warn("Converter = {} on Thread {}", BeanUtilsBean.getInstance(), Thread.currentThread());
    }
  }

  abstract public List<Color> getLedsForInformation();
  
  final public List<Color> getLedsForBlinkyTape() {
    blinkyTapeVersionCurrent = true;
    return getLedsForInformation();
  }
  
  public final void blinkyTapeVersionIsNowObsoleteWarningWillRobinsonWarning() {
    blinkyTapeVersionCurrent = false;
  }
  
  public final boolean isBlinkyTapeVersionCurrent() {
    gazeAtMyNavelAmIReallyCurrent();
    return blinkyTapeVersionCurrent;
  }
  
  public void gazeAtMyNavelAmIReallyCurrent() {
  }

  @Override
  public String toString() {
    return String.format("%s [name=%s, length=%s]", super.toString(), name, length);
  }
}
