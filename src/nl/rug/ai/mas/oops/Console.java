package nl.rug.ai.mas.oops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JTextArea;

public class Console extends JTextArea {
	public PipedInputStream d_out;
	public PipedInputStream d_err;
	
	private static class ReaderThread extends Thread {
		private BufferedReader d_input;
		private JTextArea d_console;
		
		public ReaderThread(InputStream is, JTextArea console) {
			d_input = new BufferedReader(new InputStreamReader(is));
			d_console = console;
		}

		@Override
		public void run() {
			while (true) {
				try {
					sleep(10);
				} catch (InterruptedException e) {
				
				}
				try {
					if (d_input.ready()) {
						String line = d_input.readLine();
						synchronized(d_console) {
							d_console.append(line + "\n");
						}
					}
				} catch (IOException e) {
					synchronized(d_console) {
						d_console.append("Internal error: e.getMessage()");
					}
				}
			}
		}
	}

	public Console() {
		d_out = new PipedInputStream();
		d_err = new PipedInputStream();
	}
	
	public void start() {
		Thread outThread = new ReaderThread(d_out, this);
		outThread.setDaemon(true);
		outThread.start();
		Thread errThread = new ReaderThread(d_err, this);
		errThread.setDaemon(true);
		errThread.start();
	}
	
	public OutputStream getOutputStream() throws IOException {
		return new PipedOutputStream(d_out);
	}
	
	public OutputStream getErrorStream() throws IOException {
		return new PipedOutputStream(d_err);
	}
	
	public boolean streamsFlushed() {
		try {
			return d_out.available() == 0 && d_err.available() == 0;
		} catch (IOException e) {
			return false;
		}
	}

	public void clear() {
		setText("");
	}
}