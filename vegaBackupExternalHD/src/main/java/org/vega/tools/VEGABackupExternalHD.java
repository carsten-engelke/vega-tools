package org.vega.tools;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FileUtils;

/**
 * A simple tool to backup specific folders from to a destination folder. E.g.
 * from one ext hd to another.
 */
public class VEGABackupExternalHD {

	/** The frm batch copy folders. */
	private JFrame frmBatchCopyFolders;

	/** The txt dest. */
	private JTextField txtDest;

	/** The srcs. */
	private DefaultListModel<File> srcs = new DefaultListModel<File>();

	/** The dest. */
	private static File dest = new File("E:/");

	/** The progress bar. */
	JProgressBar progressBar = new JProgressBar();

	/** The lbl new label. */
	JLabel lblNewLabel = new JLabel("Hit Start Button");

	/** The btn add source folder. */
	private JButton btnAddSourceFolder;

	/** The btn remove source folder. */
	private JButton btnRemoveSourceFolder;

	/** The btn set dest folder. */
	private JButton btnSetAimFolder;

	/** The btn start backup. */
	private JButton btnStartBackup;
	private JButton btnCleanDestination;
	private JLabel lblSources;
	private JLabel lblDestination;
	private JPanel panel;
	private JButton btnLoadSettings;
	private JButton btnSaveSettings;
	private JTextArea console;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e) {
						e.printStackTrace();
					}
					VEGABackupExternalHD window = new VEGABackupExternalHD();
					window.frmBatchCopyFolders.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application initializing all frame content.
	 */
	public VEGABackupExternalHD() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final VEGABackupExternalHD parent = this;
		frmBatchCopyFolders = new JFrame();
		frmBatchCopyFolders.setTitle("Batch Copy Folders");
		frmBatchCopyFolders.setBounds(100, 100, 450, 500);
		frmBatchCopyFolders.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 1.0, 1.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		frmBatchCopyFolders.getContentPane().setLayout(gridBagLayout);

		lblSources = new JLabel("Sources:");
		GridBagConstraints gbc_lblSources = new GridBagConstraints();
		gbc_lblSources.anchor = GridBagConstraints.WEST;
		gbc_lblSources.insets = new Insets(0, 0, 5, 5);
		gbc_lblSources.gridx = 0;
		gbc_lblSources.gridy = 0;
		frmBatchCopyFolders.getContentPane().add(lblSources, gbc_lblSources);

		lblDestination = new JLabel("Destination:");
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.anchor = GridBagConstraints.WEST;
		gbc_lblDestination.insets = new Insets(0, 0, 5, 0);
		gbc_lblDestination.gridx = 2;
		gbc_lblDestination.gridy = 0;
		frmBatchCopyFolders.getContentPane().add(lblDestination,
				gbc_lblDestination);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		frmBatchCopyFolders.getContentPane().add(scrollPane, gbc_scrollPane);

		final JList<File> list = new JList<File>();
		list.setModel(srcs);
		scrollPane.setViewportView(list);

		txtDest = new JTextField(dest.getPath());
		txtDest.setEditable(false);
		GridBagConstraints gbc_txtF = new GridBagConstraints();
		gbc_txtF.insets = new Insets(0, 0, 5, 0);
		gbc_txtF.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtF.gridx = 2;
		gbc_txtF.gridy = 1;
		frmBatchCopyFolders.getContentPane().add(txtDest, gbc_txtF);
		txtDest.setColumns(10);

		// add basic files
		loadSettings(new File("backupExtHD.config"));

		btnAddSourceFolder = new JButton("Add source folder");
		btnAddSourceFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				File f = null;
				try {
					f = srcs.get(srcs.getSize() - 1).getParentFile();
				} catch (Exception e) {
					f = dest;
				}
				JFileChooser jfc = new JFileChooser(f);
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret = jfc.showSaveDialog(frmBatchCopyFolders);
				if (ret == JFileChooser.APPROVE_OPTION) {

					srcs.addElement(jfc.getSelectedFile());

				}
			}
		});

		btnSetAimFolder = new JButton("Set destination folder");
		btnSetAimFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser jfc = new JFileChooser(dest);
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret = jfc.showSaveDialog(frmBatchCopyFolders);
				if (ret == JFileChooser.APPROVE_OPTION) {

					dest = jfc.getSelectedFile();

				}
				txtDest.setText(dest.getPath());
			}
		});
		GridBagConstraints gbc_btnSetAimFolder = new GridBagConstraints();
		gbc_btnSetAimFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetAimFolder.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetAimFolder.gridx = 2;
		gbc_btnSetAimFolder.gridy = 2;
		frmBatchCopyFolders.getContentPane().add(btnSetAimFolder,
				gbc_btnSetAimFolder);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Backup Settings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 3;
		frmBatchCopyFolders.getContentPane().add(panel, gbc_panel);

		btnLoadSettings = new JButton("Load Settings");
		btnLoadSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser(System
						.getProperty("user.dir"));
				int ret = jfc.showOpenDialog(frmBatchCopyFolders);
				if (ret == JFileChooser.APPROVE_OPTION) {

					System.out.print("Load settings from: "
							+ jfc.getSelectedFile().getPath() + "...");
					if (loadSettings(jfc.getSelectedFile())) {
						System.out.println("done");
					} else {
						System.out.println("failed");
					}
				}
			}
		});
		panel.add(btnLoadSettings);

		btnSaveSettings = new JButton("Save Settings");
		btnSaveSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser(System
						.getProperty("user.dir"));
				int ret = jfc.showSaveDialog(frmBatchCopyFolders);
				if (ret == JFileChooser.APPROVE_OPTION) {

					System.out.print("Save settings to: "
							+ jfc.getSelectedFile().getPath() + "...");
					if (saveSettings(jfc.getSelectedFile())) {
						System.out.println("done");
					} else {
						System.out.println("failed");
					}
				}
			}
		});
		panel.add(btnSaveSettings);
		GridBagConstraints gbc_btnAddSourceFolder = new GridBagConstraints();
		gbc_btnAddSourceFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddSourceFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddSourceFolder.gridx = 0;
		gbc_btnAddSourceFolder.gridy = 4;
		frmBatchCopyFolders.getContentPane().add(btnAddSourceFolder,
				gbc_btnAddSourceFolder);

		btnRemoveSourceFolder = new JButton("Remove source folder");
		btnRemoveSourceFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (list.getSelectedIndex() != -1) {

					srcs.remove(list.getSelectedIndex());
				}
			}
		});
		GridBagConstraints gbc_btnRemoveSourceFolder = new GridBagConstraints();
		gbc_btnRemoveSourceFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemoveSourceFolder.gridx = 1;
		gbc_btnRemoveSourceFolder.gridy = 4;
		frmBatchCopyFolders.getContentPane().add(btnRemoveSourceFolder,
				gbc_btnRemoveSourceFolder);

		btnStartBackup = new JButton("Start Backup");
		btnStartBackup
				.setIcon(new ImageIcon(
						VEGABackupExternalHD.class
								.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		btnStartBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new performBackupThread(parent).start();
			}
		});
		GridBagConstraints gbc_btnStartBackup = new GridBagConstraints();
		gbc_btnStartBackup.weighty = 1.0;
		gbc_btnStartBackup.fill = GridBagConstraints.BOTH;
		gbc_btnStartBackup.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartBackup.gridwidth = 3;
		gbc_btnStartBackup.gridx = 0;
		gbc_btnStartBackup.gridy = 5;
		frmBatchCopyFolders.getContentPane().add(btnStartBackup,
				gbc_btnStartBackup);

		btnCleanDestination = new JButton("Clean Destination");
		btnCleanDestination.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				new performCleanDest(parent).start();
			}
		});
		btnCleanDestination
				.setIcon(new ImageIcon(
						VEGABackupExternalHD.class
								.getResource("/com/sun/java/swing/plaf/windows/icons/Warn.gif")));
		GridBagConstraints gbc_btnCleanDestination = new GridBagConstraints();
		gbc_btnCleanDestination.insets = new Insets(0, 0, 5, 0);
		gbc_btnCleanDestination.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCleanDestination.gridwidth = 3;
		gbc_btnCleanDestination.gridx = 0;
		gbc_btnCleanDestination.gridy = 6;
		frmBatchCopyFolders.getContentPane().add(btnCleanDestination,
				gbc_btnCleanDestination);

		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 7;
		frmBatchCopyFolders.getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 3;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 8;
		frmBatchCopyFolders.getContentPane().add(progressBar, gbc_progressBar);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 9;
		frmBatchCopyFolders.getContentPane().add(scrollPane_1, gbc_scrollPane_1);

		console = new JTextArea();
		scrollPane_1.setViewportView(console);
		PrintStream out = new PrintStream(new ByteArrayOutputStream() {
			public synchronized void flush() throws IOException {
				console.setText(toString());
			}
		}, true);
		System.setOut(out);
		System.setErr(out);
	}

	boolean loadSettings(File config) {

		String[] basicFiles = new String[] { "D:/Audiobooks", "D:/Bilder",
				"D:/Ebooks", "D:/Filme", "D:/Gpx-Dateien", "D:/Musik",
				"D:/Roms" };
		srcs.removeAllElements();
		if (config.exists() && config.canRead()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(config));
				basicFiles = br.readLine().split(";");
				dest = new File(br.readLine());
				br.close();
				for (String src : basicFiles) {
					srcs.addElement(new File(src));
				}

				return true;
			} catch (IOException e1) {
				e1.printStackTrace();
				basicFiles = new String[] { "D:/Audiobooks", "D:/Bilder",
						"D:/Ebooks", "D:/Filme", "D:/Gpx-Dateien", "D:/Musik",
						"D:/Roms" };
				dest = new File("E:/");
				return false;
			}
		}
		return false;
	}

	boolean saveSettings(File config) {

		try {
			FileWriter fw = new FileWriter(config);
			String saveString = "";
			for (int i = 0; i < srcs.getSize(); i++) {

				saveString += srcs.get(i).getPath() + ";";
			}
			saveString += "\n" + txtDest.getText();
			fw.write(saveString);
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * The Class performBackupThread. Collects files from the given sources and
	 * copies them to the destination "dest". Updates the progress bar of the
	 * frame of the given parent ExtCopyBackup
	 */
	class performBackupThread extends Thread {

		/** The parent ExtCopyBackup */
		VEGABackupExternalHD parent;

		/**
		 * Instantiates a new perform backup thread.
		 * 
		 * @param parent
		 *            the parent
		 */
		public performBackupThread(VEGABackupExternalHD parent) {

			this.parent = parent;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			parent.disable();
			BusyRobot busy = new BusyRobot();
			busy.start();
			int n = 1;
			Enumeration<File> srcDirs = srcs.elements();
			progressBar.setMaximum(srcs.getSize());
			ArrayList<CopyItem> copyList = new ArrayList<VEGABackupExternalHD.CopyItem>();
			while (srcDirs.hasMoreElements()) {

				lblNewLabel.setText("reading srcs..." + n + "/"
						+ srcs.getSize());
				progressBar.setValue(n);
				File src = srcDirs.nextElement();
				try {
					addSrcFilesToList(src, src.getPath(), copyList);
				} catch (IOException e) {

					e.printStackTrace();
				}
				n++;
			}

			ArrayList<CopyItem> errorList = new ArrayList<VEGABackupExternalHD.CopyItem>();
			progressBar.setMaximum(copyList.size());
			int prg = 0;
			int copied = 0;
			for (CopyItem c : copyList) {

				prg++;
				if (prg % 100 == 0) {
					progressBar.setValue(prg);
					lblNewLabel.setText(c.toString());
				}
				if (c.aim.exists() && c.aim.length() == c.src.length()) {

					// do not copy
				} else {

					System.out.print("COPY: " + c + "...");
					try {

						FileUtils.copyFile(c.src, c.aim);
						copied++;
						System.out.println("done");
					} catch (IOException e) {

						System.out.println("failed");
						e.printStackTrace();
						errorList.add(c);
					}
				}
			}

			progressBar.setValue(progressBar.getMaximum());
			if (errorList.size() > 0) {
				try {
					FileWriter fw = new FileWriter("error.txt");
					for (CopyItem e : errorList) {
						fw.write(e.toString() + "\n");
					}
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				lblNewLabel.setText("DONE: " + copied
						+ " files copied, Errors: " + errorList.size()
						+ " - see errors.txt");
			} else {
				lblNewLabel.setText("DONE: " + copied + " files copied");
			}
			busy.keepBusy = false;
			parent.enable();
		}
	}

	class performCleanDest extends Thread {

		VEGABackupExternalHD parent;

		public performCleanDest(VEGABackupExternalHD parent) {

			this.parent = parent;
		}

		@Override
		public void run() {

			parent.disable();
			BusyRobot busy = new BusyRobot();
			busy.start();
			Enumeration<File> srcsFiles = srcs.elements();
			ArrayList<File> destFiles = new ArrayList<File>();
			int n = 1;
			progressBar.setMaximum(srcs.getSize());
			while (srcsFiles.hasMoreElements()) {

				lblNewLabel.setText("reading dest..." + n + "/"
						+ srcs.getSize());
				progressBar.setValue(n);
				File src = srcsFiles.nextElement();
				File subDest = new File(dest.getPath() + "/" + src.getName());
				try {
					addCleanFilesToList(subDest, src, subDest.getPath(),
							destFiles);
				} catch (IOException e) {
					e.printStackTrace();
				}
				n++;
			}

			new DeleteFilesDialog(destFiles, parent).setVisible(true);
			busy.keepBusy = false;
		}
	}

	/**
	 * Adds the src files to the list of copyable items
	 * 
	 * @param src
	 *            the src
	 * @param actualDir
	 *            the actual dir
	 * @param itemList
	 *            the item list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addSrcFilesToList(File src, String actualDir,
			ArrayList<CopyItem> itemList) throws IOException {

		File actualDirFile = new File(actualDir);
		if (actualDirFile.exists() && actualDirFile.isDirectory()) {

			for (File f : actualDirFile.listFiles()) {

				if (f.isDirectory()) {

					addSrcFilesToList(src, f.getAbsolutePath(), itemList);

				} else {

					File tmp_aim = new File(dest.getPath()
							+ "/"
							+ f.getPath().substring(
									src.getPath().lastIndexOf(File.separator)));
					CopyItem tmp_ci = new CopyItem(f, tmp_aim);
					itemList.add(tmp_ci);
				}
			}
		} else {

			throw new IOException("Directory not found: "
					+ actualDirFile.getAbsolutePath());
		}
	}

	/**
	 * Adds the src files to the list of copyable items
	 * 
	 * @param dest
	 *            the dest
	 * @param actualDir
	 *            the actual dir
	 * @param fileList
	 *            the file list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addCleanFilesToList(File dest, File correspSrc,
			String actualDir, ArrayList<File> fileList) throws IOException {

		File actualDirFile = new File(actualDir);
		if (actualDirFile.exists() && actualDirFile.isDirectory()) {

			for (File f : actualDirFile.listFiles()) {

				if (f.isDirectory()) {

					addCleanFilesToList(dest, correspSrc, f.getAbsolutePath(),
							fileList);

				} else {

					File tmpSrcFile = new File(correspSrc.getPath() + "/"
							+ f.getPath().substring(dest.getPath().length()));
					if (!tmpSrcFile.exists()) {
						fileList.add(f);
					}
				}
			}
		} else {

			throw new IOException("Directory not found: "
					+ actualDirFile.getAbsolutePath());
		}
	}

	/**
	 * The Class CopyItem.
	 */
	static class CopyItem {

		/**
		 * Instantiates a new copy item.
		 * 
		 * @param src
		 *            the src
		 * @param dest
		 *            the dest
		 */
		public CopyItem(File src, File aim) {

			this.src = src;
			this.aim = aim;
		}

		/** The src. */
		File src;

		/** The dest. */
		File aim;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {

			return src.getPath() + " -> " + aim.getPath();
		}
	}

	/**
	 * Disable = disable all buttons
	 */
	void disable() {

		btnAddSourceFolder.setEnabled(false);
		btnRemoveSourceFolder.setEnabled(false);
		btnSetAimFolder.setEnabled(false);
		btnStartBackup.setEnabled(false);
		btnCleanDestination.setEnabled(false);

	}

	/**
	 * Enable = enable all buttons
	 */
	void enable() {

		btnAddSourceFolder.setEnabled(true);
		btnRemoveSourceFolder.setEnabled(true);
		btnSetAimFolder.setEnabled(true);
		btnStartBackup.setEnabled(true);
		btnCleanDestination.setEnabled(true);
	}

	class BusyRobot extends Thread {

		Robot r = null;
		boolean keepBusy = true;

		public BusyRobot() {

			try {
				r = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
				r = null;
			}
		}

		@Override
		public void run() {

			System.out.println("enter busy_mode");
			Point pos = MouseInfo.getPointerInfo().getLocation();
			while (r != null && keepBusy) {
				System.out.println("execute busy_mouse_move");
				pos = MouseInfo.getPointerInfo().getLocation();
				r.mouseMove(pos.x + 1, pos.y + 1);
				r.mouseMove(pos.x, pos.y);
				try {
					sleep(60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("exit busy_mode");
		}
	}
}
