package nl.rug.ai.mas.oops;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComponent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class ScriptEditor {
	//private JTextArea d_area;
	private RTextScrollPane d_area;
	private File d_file;
	
	public ScriptEditor() {
		d_area = createEditor();
	}

	public String getText() {
		return d_area.getTextArea().getText();
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
		int read = 0;
		while ((read = r.read(chars)) > -1) {
			sb.append(String.valueOf(chars, 0, read));
		}
		r.close();
		
		d_area.getTextArea().setText(sb.toString());
	}
	
	public void clear() {
		d_area.getTextArea().setText("");
	}
	
	public void write() throws IOException {
		if (d_file == null) {
			throw new IOException("No file to write to.");
		}
		
		BufferedWriter w = new BufferedWriter(new FileWriter(d_file));
		w.write(d_area.getTextArea().getText());
		w.close();
	}
	
	private static RTextScrollPane createEditor() {
		RSyntaxTextArea editorArea = new RSyntaxTextArea();
		editorArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		//editorArea.setColumns(80);
		//editorArea.setRows(25);
		editorArea.setLineWrap(true);
		editorArea.setWrapStyleWord(true);
		editorArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
		editorArea.setAutoIndentEnabled(true);
		
		RTextScrollPane sp = new RTextScrollPane(editorArea);
		sp.setLineNumbersEnabled(true);
		
		
		return sp;
	}
	
}
