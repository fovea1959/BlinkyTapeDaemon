package com.whirlpool.isec.blinkytape.strips;

import java.awt.Color;

import com.whirlpool.isec.blinkytape.config.TapeConfig;
import com.whirlpool.isec.blinkytape.serial.BTSerial;
import com.whirlpool.isec.blinkytape.serial.BTSerialException;
import com.whirlpool.isec.blinkytape.serial.BTSerialJssc;

public class LocalBlinkyTape extends AbstractStrip {
  private BTSerial btSerial;

  public LocalBlinkyTape(TapeConfig tapeConfig) throws BTSerialException {
    super(tapeConfig);
    btSerial = new BTSerialJssc();
    send255();
  }

  @Override
  public void updateTape() throws BTSerialException {
      long fiddleTime = 0;
      byte[] bytes = new byte[getLength()*3];
      int bindex = 0;
      for (int i = 0; i < getLength(); i++) {
        long t0  = System.currentTimeMillis();
        Color c = getColorAccountingForReverse(i);
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

        long t1  = System.currentTimeMillis();
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
      long t1 = System.currentTimeMillis();
      long sendTime = t1 - t0;
      logger.debug ("update breakdown: fiddling={}, sending={}", fiddleTime, sendTime);
    }


  @Override
  public void close() throws BTSerialException {
    btSerial.close();
    super.close();
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
      Thread.sleep(5);
    } catch (InterruptedException ex) {
    }
  }
  

  


}
