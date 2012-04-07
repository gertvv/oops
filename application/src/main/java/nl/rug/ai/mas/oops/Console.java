package nl.rug.ai.mas.oops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Console extends JTextArea {
	public PipedInputStream d_out;
	public PipedInputStream d_err;
	
	private static class ReaderThread extends Thread {
		private List<BufferedReader> d_input = new ArrayList<BufferedReader>();
		private JTextArea d_console;
		
		public ReaderThread(JTextArea console) {
			d_console = console;
		}

		public void addInputStream(InputStream is) {
			d_input.add(new BufferedReader(new InputStreamReader(is)));
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					sleep(10);
				} catch (InterruptedException e) {
				
				}
				try {
					for (BufferedReader reader : d_input) {
						if (reader.ready()) {
							String line = reader.readLine();
							synchronized(d_console) {
								d_console.append(line + "\n");
								
								// Scroll down
								d_console.setCaretPosition(d_console.getDocument().getLength());
							}
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
		ReaderThread readerThread = new ReaderThread(this);
		readerThread.addInputStream(d_out);
		readerThread.addInputStream(d_err);
		readerThread.setDaemon(true);
		readerThread.start();
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