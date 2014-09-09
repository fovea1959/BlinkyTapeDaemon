package com.whirlpool.isec.blinkytape;

import java.awt.Color;

import org.apache.commons.beanutils.converters.AbstractConverter;

import com.whirlpool.isec.blinkytape.rest.ColorParam;

public class ColorConverter extends AbstractConverter {

  @Override
  protected Object convertToType(Class type, Object value) throws Throwable {
    return new ColorParam(value.toString());
  }

  @Override
  protected Class getDefaultType() {
    return Color.class;
  }

}
