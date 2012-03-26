package nl.rug.ai.mas.oops;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import nl.rug.ai.mas.oops.lua.LuaProver;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private ScriptEditor d_editorArea;
	private Console d_console;
	private JMenuItem d_saveItem;
	private JMenuItem d_refreshItem;

	public GUI() {
		super("OOPS Graphical Environment");
		
		setJMenuBar(buildMenuBar());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				quitApplication();
			}
			
			public void windowClosed(WindowEvent e) { }
			public void windowActivated(WindowEvent e) { }
			public void windowDeactivated(WindowEvent e) { }
			public void windowDeiconified(WindowEvent e) { }
			public void windowIconified(WindowEvent e) { }
			public void windowOpened(WindowEvent e) { }
		});
		
		JFrame panel = this;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipadx = 5;
		c.ipady = 5;
		panel.add(new JLabel("Editor"), c);
		c.gridx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(new JLabel("---"), c);
		
		c.gridy = 2;
		c.gridx = 1;
		c.gridwidth = 2;
		c.weighty = 0.7;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane editorPane = new JScrollPane();
		editorPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		d_editorArea = new ScriptEditor();
		editorPane.setViewportView(d_editorArea.getComponent());
		panel.add(editorPane, c);
		
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.0;
		c.weightx = 0.0;
		panel.add(new JLabel("Console Output"), c);
		
		c.gridy = 4;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.3;
		c.weightx = 1.0;
		JScrollPane consolePane = new JScrollPane();
		d_console = new Console();
		d_console.setFont(new Font("Monospaced", Font.PLAIN, 11));
		d_console.setEditable(false);
		consolePane.setViewportView(d_console);
		panel.add(consolePane, c);
		
		d_console.start();
		try {
			System.setOut(new PrintStream(d_console.getOutputStream()));
			System.setErr(new PrintStream(d_console.getErrorStream()));
		} catch (IOException e) {
			showError(e, "Error Initializing Console");
		}
	}

	private void showError(Exception e, String title) {
		JOptionPane.showMessageDialog(this, title + " \n" + e.getMessage(),
				title, JOptionPane.ERROR_MESSAGE);
	}

	protected void quitApplication() {
		this.dispose();
		System.exit(0);
	}

	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(buildFileMenu());
		menuBar.add(buildRunMenu());
		return menuBar;
	}

	private JMenu buildRunMenu() {
		JMenu runMenu = new JMenu("Run");
		
		JMenuItem runItem = buildMenuItem("Execute", 'E', KeyEvent.VK_E, false);
		runItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runEditorContents();
			}
		});
		runMenu.add(runItem);
		JMenuItem clearItem = buildMenuItem("Clear console", 'C', KeyEvent.VK_E, true);
		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearConsole();
			}
		});
		runMenu.add(clearItem);
		
		return runMenu;
	}

	protected void runEditorContents() {
		String text = d_editorArea.getText();
		LuaProver prover = new LuaProver();
		prover.doStream(new ByteArrayInputStream(text.getBytes()), "EditorContents");
		while (!d_console.streamsFlushed()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				showError(e);
			}
		}
		System.err.println("\n  ============================= Run completed =============================\n");
	}
	
	protected void clearConsole() {
		d_console.clear();
	}

	private JMenu buildFileMenu() {
		JMenu fileMenu = new JMenu("File");
		
		// New file
		JMenuItem newItem = buildMenuItem("New", 'N', KeyEvent.VK_N, false);
		newItem.setEnabled(true);
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newFile();
			}
		});
		fileMenu.add(newItem);
		
		// Open file
		JMenuItem openItem = buildMenuItem("Open", 'O', KeyEvent.VK_O, false);
		openItem.setEnabled(true);
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFile();
			}
		});
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		
		// Save file
		JMenuItem saveItem = buildMenuItem("Save", 'S', KeyEvent.VK_S, false);
		saveItem.setEnabled(false);
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		fileMenu.add(saveItem);
		d_saveItem = saveItem;
		
		// Save file as
		JMenuItem saveAsItem = buildMenuItem("Save As", 'A', KeyEvent.VK_S, true);
		saveAsItem.setEnabled(true);
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFileAs();
			}
		});
		fileMenu.add(saveAsItem);
		
		fileMenu.addSeparator();
		
		// Refresh file (reload from file system)
		JMenuItem refreshItem = buildMenuItem("Refresh", 'R', KeyEvent.VK_R, false);
		refreshItem.setEnabled(false);
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshFile();
			}
		});
		fileMenu.add(refreshItem);
		d_refreshItem = refreshItem;
		
		fileMenu.addSeparator();
		
		// Exit program
		JMenuItem exitItem = buildMenuItem("Exit", 'E', KeyEvent.VK_Q, false);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				quitApplication();
			}
		});
		fileMenu.add(exitItem);
		
		return fileMenu;
	}
	
	private void refreshFile() {
		try {
			d_editorArea.load();
		} catch (IOException e) {
			showError(e);
		}
		updateEnabledMenuItems();
	}

	private void saveFileAs() {
		JFileChooser fc = new JFileChooser();
		int result = fc.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			d_editorArea.setFile(fc.getSelectedFile());
			try {
				d_editorArea.write();
			} catch (IOException e) {
				showError(e);
			}
		}
		updateEnabledMenuItems();
	}

	private void saveFile() {
		try {
			d_editorArea.write();
		} catch (IOException e) {
			showError(e);
		}
		updateEnabledMenuItems();
	}

	private void openFile() {
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			d_editorArea.setFile(fc.getSelectedFile());
			try {
				d_editorArea.load();
			} catch (IOException e) {
				showError(e);
			}
		}
		updateEnabledMenuItems();
	}

	private void showError(Exception e) {
		showError(e, "Error");
	}

	private void newFile() {
		d_editorArea.setFile(null);
		d_editorArea.clear();
		updateEnabledMenuItems();
	}

	private void updateEnabledMenuItems() {
		boolean enabled = d_editorArea.getFile() != null;
		d_saveItem.setEnabled(enabled);
		d_refreshItem.setEnabled(enabled);
	}

	/**
	 * Build a menu item without key accelerator.
	 * @param title
	 * @param mnemnonic
	 * @return
	 */
	private JMenuItem buildMenuItem(String title, char mnemonic) {
		JMenuItem item = new JMenuItem(title);
		item.setMnemonic(mnemonic);
		return item;
	}

	/**
	 * Build a menu item with key accelerator.
	 * @param title
	 * @param mnemonic
	 * @param accelerator
	 * @param shift
	 * @return
	 */
	private JMenuItem buildMenuItem(String title, char mnemonic, int accelerator, boolean shift) {
		JMenuItem item = buildMenuItem(title, mnemonic);
		int keyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		if (shift) {
			keyMask = keyMask | KeyEvent.SHIFT_MASK;
		}
		item.setAccelerator(KeyStroke.getKeyStroke(accelerator,
				keyMask, false));
		return item;
	}
	
	public static void main(String args[]) {
		JFrame window = new GUI();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setMinimumSize(new Dimension(200, 300));
		window.setSize(new Dimension(550, 700));
		window.setVisible(true);
	}
}
