package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;

import com.whirlpool.isec.blinkytape.rest.ColorParam;

public class SegmentBar extends Segment {
  Color color = Color.WHITE;

  Integer value = 0;

  // why am I doing this twice? once in config, once here?
  static {
    //ConvertUtils.register(new ColorConverter(), Color.class);
  }

  @Override
  public void setValues(MultivaluedMap<String, String> m) {
    //ConvertUtils.register(new ColorConverter(), Color.class);
    for (String name : m.keySet()) {
      String v = m.getFirst(name);
      try {
        logger.warn ("Converter = {}", BeanUtilsBean.getInstance());
        BeanUtils.setProperty(this, name, v);
      } catch (IllegalAccessException | InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      /*
      if (name.equals("color")) {
        color = new ColorParam(v);
      } else if (name.equals("value")) {
        value = (new Double(v)).intValue();
      } else {
        throw new WebApplicationException("Unknown attribute = '" + name + "'", 404);
      }
      */
    }
  }

  @Override
  public List<Color> getLeds() {
    List<Color> rv = new ArrayList<Color>(getLength());
    for (int i = 0; i < getLength(); i++) {
      Color c1 = Color.black;
      if (i < value)
        c1 = color;
      rv.add(c1);
    }
    return rv;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

}
