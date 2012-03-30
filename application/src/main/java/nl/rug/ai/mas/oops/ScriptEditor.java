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
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class ScriptEditor {

	/**
	 * Shared interface for editor types
	 * 
	 */
	private interface EditorInterface {
		void undo();

		void redo();

		void cut();

		void copy();

		void paste();

		boolean isStyled();

		String getText();

		void setText(String newText);

		JComponent getComponent();
	}

	/**
	 * Compound undo manager. Combines multiple edits into one if they were
	 * typed together. This fixes Java's default one-character-at-a-time
	 * behavior.
	 * 
	 */
	private class CUndoManager extends UndoManager {
		private static final long serialVersionUID = -4215454307550640398L;

		private CompoundEdit compoundEdit = null;
		private final JTextArea textArea;
		private int lastOffset = 0;
		private int lastLine = 0;

		public CUndoManager(JTextArea textArea) {
			this.textArea = textArea;
		}

		public void redo() throws CannotRedoException {
			// Forcefully end the current compound edit to make it
			// available to the undo/redo system

			if (compoundEdit != null) {
				compoundEdit.end();
				compoundEdit = null;
			}
			super.redo();
		}

		private CompoundEdit startCompoundEdit(UndoableEdit edit) {
			lastOffset = textArea.getCaretPosition();
			try {
				lastLine = textArea.getLineOfOffset(lastOffset);
			} catch (BadLocationException e) {
				lastLine = -1;
			}
			compoundEdit = new CompoundEdit();
			compoundEdit.addEdit(edit);
			addEdit(compoundEdit);
			return compoundEdit;
		}

		public void undo() throws CannotUndoException {

			// Forcefully end the current compound edit to make it
			// available to the undo/redo system
			if (compoundEdit != null) {
				compoundEdit.end();
				compoundEdit = null;
			}
			super.undo();
		}

		public void undoableEditHappened(UndoableEditEvent e) {

			if (compoundEdit == null) {
				compoundEdit = startCompoundEdit(e.getEdit());
				return;
			}

			// Check if the caret moved more than one position, or if
			// it changed lines. If so, start a new compound edit.
			
			int diff = textArea.getCaretPosition() - lastOffset;
			int currentLine;

			try {
				currentLine = textArea.getLineOfOffset(textArea.getCaretPosition());
			} catch (BadLocationException e1) {
				currentLine = -1;
			}

			if (Math.abs(diff) <= 1 && currentLine == lastLine) {
				compoundEdit.addEdit(e.getEdit());
				lastOffset += diff;
				return;
			}

			compoundEdit.end();
			compoundEdit = startCompoundEdit(e.getEdit());
		}
	}

	/**
	 * Plain text editor (JTextEditor with undo/redo support)
	 *
	 */
	private class PlainTextEditor implements EditorInterface {
		private final CUndoManager d_undoManager;
		private final JTextArea d_textArea;

		public PlainTextEditor() {
			d_textArea = new JTextArea();
			d_undoManager = new CUndoManager(d_textArea);

			d_textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
			d_textArea.setLineWrap(true);
			d_textArea.setWrapStyleWord(true);

			d_textArea.getDocument().addUndoableEditListener(d_undoManager);
		}

		public boolean isStyled() {
			return false;
		}

		public void undo() {
			try {
				d_undoManager.undo();
			} catch (Exception e) {
			}
		}

		public void redo() {
			try {
				d_undoManager.redo();
			} catch (Exception e) {
			}
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
			d_undoManager.discardAllEdits();
		}

		public JComponent getComponent() {
			return d_textArea;
		}
	}

	
	/**
	 * Styled (Syntax Highlighted) editor, using RSyntaxTextArea
	 * with Lua syntax highlighting.
	 *
	 */
	private class StyledTextEditor implements EditorInterface {
		private final RSyntaxTextArea d_textArea;
		private final RTextScrollPane d_scrollPane;

		public StyledTextEditor() {
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
	}

	private EditorInterface d_area;
	private File d_file;

	public ScriptEditor(boolean styled) {
		if (styled) {
			d_area = new StyledTextEditor();
		} else {
			d_area = new PlainTextEditor();
		}
	}

	public String getText() {
		return d_area.getText();
	}

	public void setText(String newText) {
		d_area.setText(newText);
	}

	public void undo() {
		d_area.undo();
	}

	public void redo() {
		d_area.redo();
	}

	public void cut() {
		d_area.cut();
	}

	public void copy() {
		d_area.copy();
	}

	public void paste() {
		d_area.paste();
	}

	public boolean isStyled() {
		return d_area.isStyled();
	}

	public JComponent getComponent() {
		return d_area.getComponent();
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
		w.write(d_area.getText());
		w.close();
	}
}
