package com.whirlpool.isec.blinkytape.data;
import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.*;

public class Datum {
  static Logger staticLogger = LoggerFactory.getLogger(Datum.class);
  Logger logger = null;
  
  private long lastChangedAt = 0;
  
  private String name;
  
  public Datum (String name) {
    this.name = name;
    logger = LoggerFactory.getLogger(Datum.class.getName() + "." + name);
  }
  
  public String getName() {
    return name;
  }
  
  public void setValues(MultivaluedMap<String, String> m) {
    // ConvertUtils.register(new ColorConverter(), Color.class);
    logger.debug("Converter = {}", BeanUtilsBean.getInstance());
    logger.debug("incoming = {}", m.toString());
    logger.debug("before = {}", this.toString());
    for (String name : m.keySet()) {
      String v = m.getFirst(name);
      setValue(name, v);
    }
    logger.debug("after  = {}", this.toString());
    // a fair bet here
    lastChangedAt = System.currentTimeMillis();
  }

  public void setValue(String name, String v) {
    logger.debug("setting {} to {}", name, v);
    try {
      // logger.warn("Before 1 {} = {}", name, getParameters().toString());
      BeanUtils.setProperty(this, name, v);
      // logger.warn("After 1 = {}", getParameters().toString());
    } catch (InvocationTargetException | IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      logger.warn("Converter = {} on Thread {}", BeanUtilsBean.getInstance(), Thread.currentThread());
    }
  }
  
  public void markChanged() {
    markChanged(System.currentTimeMillis());
  }
  
  public void markChanged(long when) {
    lastChangedAt = when;
  }

  public long getLastChangedAt() {
    return lastChangedAt;
  }

}
