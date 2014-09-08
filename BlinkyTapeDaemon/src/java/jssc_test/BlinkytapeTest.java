package jssc_test;

import java.awt.Color;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * 
 * @author scream3r
 */
public class BlinkytapeTest {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		SerialPort serialPort = new SerialPort("COM11");
		try {
			System.out.println("Port opened: " + serialPort.openPort());
			System.out.println("Params setted: "
					+ serialPort.setParams(115200, 8, 1, 0));
			serialPort.writeByte((byte) 255);

			float phase = 0;

			while (phase < 1000) {
				for (int i = 0; i < 60; i++) {
					double budge = .3;
					double rr = (Math.sin(phase*.9  +i*budge)+1.0)*127.0;
					double gg  = (Math.sin(phase     + Math.PI/2 +i*budge)+1)*127;
					double bb = (Math.sin(phase*1.1 + Math.PI   +i*budge)+1)*127;
					// System.out.printf("position %d = %.0f, %.0f, %.0f\n", i, rr, gg, bb);
					Color c = new Color((int) rr,
		                    (int) gg,
		                    (int) bb);

					int r, g, b;
					r = c.getRed();
					g = c.getGreen();
					b = c.getBlue();
					// System.out.printf("position %d = %d, %d, %d\n", i, r, g, b);

					// Send the color for the current LED to the strip,
					// being careful not to send 255 (because that would
					// cause the strip to display the pixels
					serialPort.writeByte(bc(r));
					serialPort.writeByte(bc(g));
					serialPort.writeByte(bc(b));
					// delay();
				}
				serialPort.writeByte((byte) 255);
				serialPort.writeByte((byte) 255);
				serialPort.writeByte((byte) 255);
				phase += .1;
			}
			System.out.println("Port closed: " + serialPort.closePort());
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
	}

	static byte bc(int b) {
		byte rv = (byte) b;
		if (rv > 254)
			rv = (byte) 254;
		return rv;
	}

	static void delay() {
		/*
		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
		}
		*/
	}

	static void bigdelay() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}