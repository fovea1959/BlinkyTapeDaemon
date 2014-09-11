package com.whirlpool.isec.blinkytape;

import java.awt.Color;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.rest.ColorParam;

public class Util {

  public static Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);

  public static void setupConverters() {
    ConvertUtilsBean convertUtilsBean = BeanUtilsBean.getInstance().getConvertUtils();
    convertUtilsBean.register(new ColorConverter(), Color.class);
    logger.warn("Converter = {} on Thread {}", BeanUtilsBean.getInstance(), Thread.currentThread());
  }

  static class ColorConverter extends AbstractConverter {

    @Override
    protected Object convertToType(Class type, Object value) throws Throwable {
      return new ColorParam(value.toString());
    }

    @Override
    protected Class getDefaultType() {
      return Color.class;
    }

  }
}
