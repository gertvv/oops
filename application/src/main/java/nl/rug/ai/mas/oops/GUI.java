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
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import nl.rug.ai.mas.oops.ConfigurableProver.AxiomSystem;
import nl.rug.ai.mas.oops.lua.LuaProver;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private ScriptEditor d_editor;
	private Console d_console;
	private JMenuItem d_saveItem;
	private JMenuItem d_refreshItem;
	private String d_defaultProver;

	public GUI() {
		super("OOPS Graphical Environment");

		setJMenuBar(buildMenuBar());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				quitApplication();
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}
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

		c.gridy = 2;
		c.gridx = 1;
		c.gridwidth = 2;
		c.weighty = 0.7;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		d_editor = new ScriptEditor();
		panel.add(d_editor.getComponent(), c);

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

		// Set default prover to S5
		d_defaultProver = "S5";
		
		d_console.start();
		try {
			System.setOut(new PrintStream(d_console.getOutputStream()));
			System.setErr(new PrintStream(d_console.getErrorStream()));
		} catch (IOException e) {
			showError(e, "Error Initializing Console");
		}
	}

	private void showError(Exception e, String title) {
		JOptionPane.showMessageDialog(this, title + " \n" + e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
	}

	protected void quitApplication() {
		this.dispose();
		System.exit(0);
	}

	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(buildFileMenu());
		menuBar.add(buildEditMenu());
		menuBar.add(buildRunMenu());
		menuBar.add(buildHelpMenu());
		return menuBar;
	}

	private JMenu buildHelpMenu() {
		try {
			URL url = HelpSet.findHelpSet(GUI.class.getClassLoader(), "nl/rug/ai/mas/oops/OopsHelp.hs");
			HelpSet hs = new HelpSet(null, url);
			
			JMenu menu = new JMenu("Help");
			JMenuItem item = new JMenuItem("Show Help");
			menu.add(item);
			item.addActionListener(new CSH.DisplayHelpFromSource(hs.createHelpBroker()));
			
			return menu;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	private JMenu buildRunMenu() {
		JMenu runMenu = new JMenu("Run");
		
		ButtonGroup group = new ButtonGroup();
		
		JMenu proversMenu = new JMenu("Default prover");
		for (final AxiomSystem system : ConfigurableProver.AxiomSystem.values())
		{
			JRadioButtonMenuItem systemItem = new JRadioButtonMenuItem(system.name(), system.name() == "S5" ? true : false);
			systemItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					d_defaultProver = system.name();
				}
			});
			
			group.add(systemItem);
			proversMenu.add(systemItem);
		}
		
		runMenu.add(proversMenu);
		runMenu.addSeparator();

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
		String text = d_editor.getText();
		LuaProver prover = new LuaProver(d_defaultProver);
		prover.doStream(new ByteArrayInputStream(text.getBytes()), "EditorContents");

		while (!d_console.streamsFlushed()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				showError(e);
			}
		}

		System.out.println("\n  ============================= Run completed =============================\n");		
	}

	protected void clearConsole() {
		d_console.clear();
	}

	private JMenu buildEditMenu() {
		JMenu editMenu = new JMenu("Edit");

		JMenuItem undoItem = buildMenuItem("Undo", 'U', KeyEvent.VK_Z, false);
		undoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				d_editor.undo();
			}
		});

		editMenu.add(undoItem);
		JMenuItem redoItem = buildMenuItem("Redo", 'R', KeyEvent.VK_Y, false);
		redoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				d_editor.redo();
			}
		});
		editMenu.add(redoItem);

		editMenu.addSeparator();

		JMenuItem cutItem = buildMenuItem("Cut", 't', KeyEvent.VK_X, false);
		cutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				d_editor.cut();
			}
		});
		editMenu.add(cutItem);

		JMenuItem copyItem = buildMenuItem("Copy", 'C', KeyEvent.VK_C, false);
		copyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				d_editor.copy();
			}
		});
		editMenu.add(copyItem);

		JMenuItem pasteItem = buildMenuItem("Paste", 'P', KeyEvent.VK_V, false);
		pasteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				d_editor.paste();
			}
		});
		editMenu.add(pasteItem);

		return editMenu;
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
			d_editor.load();
		} catch (IOException e) {
			showError(e);
		}
		updateEnabledMenuItems();
	}

	private void saveFileAs() {
		JFileChooser fc = new JFileChooser();
		int result = fc.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			d_editor.setFile(fc.getSelectedFile());
			try {
				d_editor.write();
			} catch (IOException e) {
				showError(e);
			}
		}
		updateEnabledMenuItems();
	}

	private void saveFile() {
		try {
			d_editor.write();
		} catch (IOException e) {
			showError(e);
		}
		updateEnabledMenuItems();
	}

	private void openFile() {
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			d_editor.setFile(fc.getSelectedFile());
			try {
				d_editor.load();
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
		d_editor.setFile(null);
		d_editor.clear();
		updateEnabledMenuItems();
	}

	private void updateEnabledMenuItems() {
		boolean enabled = d_editor.getFile() != null;
		d_saveItem.setEnabled(enabled);
		d_refreshItem.setEnabled(enabled);
	}

	/**
	 * Build a menu item without key accelerator.
	 * 
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
	 * 
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

		item.setAccelerator(KeyStroke.getKeyStroke(accelerator, keyMask, false));
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
