package nl.rug.ai.mas.oops;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class ScriptEditor {
	private JTextArea d_area;
	private File d_file;
	
	public ScriptEditor() {
		d_area = createEditor();
	}

	public String getText() {
		return d_area.getText();
	}
	
	public JComponent getComponent() {
		return d_area;
	}
	
	public void setFile(File f) {
		d_file = f;
	}
	
	public File getFile() {
		return d_file;
	}
	
	public void load() throws IOException {
		if (d_file == null) {
			throw new IOException("No file to read from.");
		}
		BufferedReader r = new BufferedReader(new FileReader(d_file));
		StringBuffer sb = new StringBuffer(1024);
		char[] chars = new char[1024];
		while (r.read(chars) > -1) {
			sb.append(String.valueOf(chars));
		}
		r.close();
		
		d_area.setText(sb.toString());
	}
	
	public void clear() {
		d_area.setText("");
	}
	
	public void write() throws IOException {
		if (d_file == null) {
			throw new IOException("No file to write to.");
		}
		
		BufferedWriter w = new BufferedWriter(new FileWriter(d_file));
		w.write(d_area.getText().replace("\0", ""));
		w.close();
	}
	
	private static JTextArea createEditor() {
		JTextArea editorArea = new JTextArea();
		editorArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		editorArea.setColumns(80);
		editorArea.setRows(25);
		editorArea.setLineWrap(true);
		editorArea.setWrapStyleWord(true);
		return editorArea;
	}
	
}
