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
	private final RSyntaxTextArea d_textArea;
	private final RTextScrollPane d_scrollPane;

	private File d_file;

	public ScriptEditor() {
		d_textArea = new RSyntaxTextArea();
		d_textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		d_textArea.setLineWrap(true);
		d_textArea.setWrapStyleWord(true);
		d_textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
		d_textArea.setAutoIndentEnabled(true);

		d_scrollPane = new RTextScrollPane(d_textArea);
		d_scrollPane.setLineNumbersEnabled(true);
	}

	public boolean isStyled() {
		return true;
	}

	public void undo() {
		d_textArea.undoLastAction();
	}

	public void redo() {
		d_textArea.redoLastAction();
	}

	public void cut() {
		d_textArea.cut();
	}

	public void copy() {
		d_textArea.copy();
	}

	public void paste() {
		d_textArea.paste();
	}

	public String getText() {
		return d_textArea.getText();
	}

	public void setText(String newText) {
		d_textArea.setText(newText);
		d_textArea.discardAllEdits();
	}

	public JComponent getComponent() {
		return d_scrollPane;
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

		setText(sb.toString());
	}

	public void clear() {
		setText("");
	}

	public void write() throws IOException {
		if (d_file == null) {
			throw new IOException("No file to write to.");
		}

		BufferedWriter w = new BufferedWriter(new FileWriter(d_file));
		w.write(getText());
		w.close();
	}
}
