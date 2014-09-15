package com.whirlpool.isec.blinkytape;

import java.awt.Color;
import java.lang.reflect.Field;

import javax.ws.rs.WebApplicationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

  public static Logger logger = LoggerFactory.getLogger(Util.class);

  public static void setupConverters() {
    ConvertUtilsBean convertUtilsBean = BeanUtilsBean.getInstance().getConvertUtils();
    convertUtilsBean.register(new ColorConverter(), Color.class);
    logger.info("Setting up converter {} on thread {}", BeanUtilsBean.getInstance(),
        Thread.currentThread());
  }

  static class ColorConverter extends AbstractConverter {

    @Override
    protected Object convertToType(@SuppressWarnings("rawtypes") Class type, Object value)
        throws Throwable {
      Object o = new ColorParam(value.toString());
      logger.debug("{} -> {}", value, o);
      return o;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class getDefaultType() {
      return Color.class;
    }

  }

  @SuppressWarnings("serial")
  static class ColorParam extends Color {
    static Logger logger = LoggerFactory.getLogger(ColorParam.class);

    public ColorParam(String s) {
      super(getRGB(s));
    }

    private static int getRGB(String s) {
      // logger.info("got color string '{}'", s);
      if (s.charAt(0) == '#') {
        try {
          Color c = Color.decode("0x" + s.substring(1));
          return c.getRGB();
        } catch (NumberFormatException e) {
          WebApplicationException we = new WebApplicationException("Invalid color "
              + "code = '" + s + "'", e, 400);
          logger.error("", we);
          throw we;
        }
      } else {
        try {
          Field f = Color.class.getField(s);
          return ((Color) f.get(null)).getRGB();
        } catch (Exception e) {
          WebApplicationException we = new WebApplicationException("Invalid color "
              + "code = '" + s + "'", e, 400);
          logger.error("", we);
          throw we;
        }
      }
    }
  }

  final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }
}
