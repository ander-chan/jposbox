package org.takepos.jposbox;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;



/**
 * Serial port event listener.
 * 
 * @author Sergio
 *
 */
public class WeighingScaleEventListener implements SerialPortEventListener {

	static final Logger log = Logger.getLogger(WeighingScaleEventListener.class.getName());

	/**
	 * port's logical name (e.g.: com1 or com2)
	 */
	private String portName;

	/**
	 * Open port
	 */
	private SerialPort port;

	/**
	 * Port's meta information.
	 */
	private CommPortIdentifier portIdentifier;

	/**
	 * Main constructor..
	 * 
	 * @param port name of the port to connect to
	 * @param retorno
	 */
	 WeighingScaleEventListener(String port) {
		this.portName = port;
	}

	
	/**
	 * Close connection to the port
	 */
	public void close() {
		if (port != null) {
			log.log(Level.INFO, "Closing serial connection {0}... ", portName);
			port.close();
			port = null;
		}
	}

	/**
	 * open port and wait for data.
	 * 
	 * @throws Exception
	 * @throws PortInUseException
	 */
	public void initializePort() throws Exception {
		try {
			this.portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			this.port = (SerialPort) portIdentifier.open("WeighingSc", RandomUtils.nextInt(0, 2000));
			port.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			port.addEventListener(this);
			port.notifyOnDataAvailable(true);

		} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | TooManyListenersException e) {
			if (port != null) {
				port.close();
			}
			throw e;
		}
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		InputStream is;
		try {
			is = port.getInputStream();
			int value;
			byte[] result = new byte[0];
			while ((value = is.read()) != -1) {
				result = ArrayUtils.add(result, (byte) value);
			}

			String stringValue = new String(result, "utf-8");
			log.log(Level.INFO, "Received value ({0}): \"{1}\"", new Object[]{stringValue.length(), stringValue});
		} catch (IOException e) {
			log.log(Level.SEVERE,e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public String getPortName() {
		return portName;
	}
	
	public void setPortName(String portName) {
		this.portName = portName;
	}

	
	public CommPortIdentifier getPortIdentifier() {
		return portIdentifier;
	}

}
