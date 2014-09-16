package com.whirlpool.isec.blinkytape.tapes;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlpool.isec.blinkytape.Util;
import com.whirlpool.isec.blinkytape.serial.BTSerial;
import com.whirlpool.isec.blinkytape.serial.BTSerialException;
import com.whirlpool.isec.blinkytape.serial.BTSerialJssc;

public class LocalBlinkyTape implements ITape {
  static Logger logger = LoggerFactory.getLogger(LocalBlinkyTape.class);
  private BTSerial btSerial;
  
  private boolean reverse = false;

  public LocalBlinkyTape() throws BTSerialException {
    super();
    btSerial = new BTSerialJssc();
    send255();
  }

  public void update(Color[] leds) throws BTSerialException {
    long fiddleTime = 0;
    int n = leds.length;
    byte[] bytes = new byte[n * 3];
    int bindex = 0;
    for (int i = 0; i < n; i++) {
      long t0 = System.currentTimeMillis();
      int ii = i;
      if (reverse) ii = (n - 1) - i;
      Color c = leds[ii];
      if (c == null)
        c = Color.BLACK;
      int r, g, b;
      r = c.getRed();
      g = c.getGreen();
      b = c.getBlue();
      // System.out.printf("position %d = %d, %d, %d\n", i, r, g, b);

      // Send the color for the current LED to the strip,
      // being careful not to send 255 (because that would
      // cause the strip to display the pixels

      bytes[bindex++] = bc(r);
      bytes[bindex++] = bc(g);
      bytes[bindex++] = bc(b);

      long t1 = System.currentTimeMillis();
      fiddleTime += (t1 - t0);
    }
    long t0 = System.currentTimeMillis();

    int offset = 0;
    while (offset < bytes.length) {
      int length = Math.min(50, bytes.length - offset);
      byte[] batch = new byte[length];
      System.arraycopy(bytes, offset, batch, 0, length);
      offset += length;
      btSerial.writeBytes(batch);
      delay();
    }
    send255();
    byte[] b = btSerial.readAvailableBytes();
    if (b != null && b.length > 0) {
      String s = Util.bytesToHex(b);
      logger.debug("{} bytes in buffer *from* BlinkyTape {}: {}", s.length(), btSerial.getPortName(), s);
      if (logger.isWarnEnabled()) {
        for (byte b1 : b) {
          if (b1 != 0x0f)  {
            logger.warn("a non-0F byte from the BlinkyTape {}: {}", btSerial.getPortName(), s);
          }
        }
      }
    }
    long t1 = System.currentTimeMillis();
    long sendTime = t1 - t0;
    logger.debug("update breakdown: fiddling={}, sending={}", fiddleTime, sendTime);
  }

  public void close() throws BTSerialException {
    btSerial.close();
  }

  void send255() throws BTSerialException {
    btSerial.writeByte((byte) 255);
    btSerial.writeByte((byte) 255);
    btSerial.writeByte((byte) 255);
    delay();
  }

  static byte bc(int b) {
    int rv = b;
    if (rv > 254)
      rv = 254;
    return (byte) rv;
  }

  static void delay() {
    try {
      Thread.sleep(3);
    } catch (InterruptedException ex) {
    }
  }

  public boolean isReverse() {
    return reverse;
  }

  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }

}
