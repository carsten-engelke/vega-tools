package org.vega.tools;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.vega.tools.backup.gui.UI_Utils;

public class GUI_FTP_Renamer {

	private JFrame frmRenameFtpFiles;
	private JLabel lblFind;
	private JTextField txtFind;
	private JLabel lblErsetzen;
	private JTextField txtReplace;
	private JCheckBox chckbxRenameDirectories;
	private JCheckBox chckbxRenameFiles;
	private JButton btnTestRenaming;
	private JButton btnRename;
	private DefaultTableModel myModel;
	private boolean ftpServerRead = false;
	private JPanel panel;
	private JLabel label;
	private JTextField txtDir;
	private JLabel label_1;
	private JTextField txtHostaddress;
	private JLabel label_2;
	private JTextField txtPort;
	private JLabel label_3;
	private JTextField txtName;
	private JLabel label_4;
	private JPasswordField pwdPassword;
	private JButton button;
	private JTable table;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
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
					GUI_FTP_Renamer window = new GUI_FTP_Renamer();
					window.frmRenameFtpFiles.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_FTP_Renamer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRenameFtpFiles = new JFrame();
		frmRenameFtpFiles.setTitle("Rename FTP Files");
		frmRenameFtpFiles.setBounds(100, 100, 680, 300);
		frmRenameFtpFiles.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		frmRenameFtpFiles.getContentPane().setLayout(gridBagLayout);
		myModel = new DefaultTableModel(new Object[][] {}, new String[] {
				"Before", "After" }) {
			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, true };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};

		panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "FTP Server",

		TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmRenameFtpFiles.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 82, 86, 0 };
		gbl_panel.rowHeights = new int[] { 20, 20, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		label = new JLabel("Directory");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);

		txtDir = new JTextField();
		txtDir.setText("/Hitachi-HDT721010SLA360-01/Documents/Ebooks/Attack_On_Titan");
		txtDir.setColumns(10);
		GridBagConstraints gbc_txtDir = new GridBagConstraints();
		gbc_txtDir.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDir.insets = new Insets(0, 0, 5, 0);
		gbc_txtDir.gridx = 1;
		gbc_txtDir.gridy = 0;
		panel.add(txtDir, gbc_txtDir);

		label_1 = new JLabel("FTP-Hostaddress");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		panel.add(label_1, gbc_label_1);

		txtHostaddress = new JTextField();
		txtHostaddress.setText("8wmrm47ir5dfajuz.myfritz.net");
		txtHostaddress.setColumns(10);
		GridBagConstraints gbc_txtHostaddress = new GridBagConstraints();
		gbc_txtHostaddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHostaddress.insets = new Insets(0, 0, 5, 0);
		gbc_txtHostaddress.gridx = 1;
		gbc_txtHostaddress.gridy = 1;
		panel.add(txtHostaddress, gbc_txtHostaddress);

		label_2 = new JLabel("FTP-Port");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		panel.add(label_2, gbc_label_2);

		txtPort = new JTextField();
		txtPort.setText("21");
		txtPort.setColumns(10);
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPort.insets = new Insets(0, 0, 5, 0);
		gbc_txtPort.gridx = 1;
		gbc_txtPort.gridy = 2;
		panel.add(txtPort, gbc_txtPort);

		label_3 = new JLabel("FTP-Name");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 3;
		panel.add(label_3, gbc_label_3);

		txtName = new JTextField();
		txtName.setText("carsten.engelke");
		txtName.setColumns(10);
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 3;
		panel.add(txtName, gbc_txtName);

		label_4 = new JLabel("FTP-Password");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 4;
		panel.add(label_4, gbc_label_4);

		pwdPassword = new JPasswordField();
		GridBagConstraints gbc_pwdPassword = new GridBagConstraints();
		gbc_pwdPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdPassword.insets = new Insets(0, 0, 5, 0);
		gbc_pwdPassword.gridx = 1;
		gbc_pwdPassword.gridy = 4;
		panel.add(pwdPassword, gbc_pwdPassword);

		button = new JButton("Read FTP-Directory");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (checkFTP()) {

					readFTPServer();
				} else {

					JOptionPane
							.showConfirmDialog(
									frmRenameFtpFiles,
									new JLabel(
											"FTP connection error. Please check your input."),
									"FTP Check", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.fill = GridBagConstraints.HORIZONTAL;
		gbc_button.gridx = 1;
		gbc_button.gridy = 5;
		panel.add(button, gbc_button);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 0;
		frmRenameFtpFiles.getContentPane().add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(myModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		lblFind = new JLabel("Find:");
		GridBagConstraints gbc_lblFind = new GridBagConstraints();
		gbc_lblFind.anchor = GridBagConstraints.EAST;
		gbc_lblFind.insets = new Insets(0, 0, 5, 5);
		gbc_lblFind.gridx = 0;
		gbc_lblFind.gridy = 1;
		frmRenameFtpFiles.getContentPane().add(lblFind, gbc_lblFind);

		txtFind = new JTextField();
		txtFind.setText("Find");
		GridBagConstraints gbc_txtFind = new GridBagConstraints();
		gbc_txtFind.insets = new Insets(0, 0, 5, 5);
		gbc_txtFind.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFind.gridx = 1;
		gbc_txtFind.gridy = 1;
		frmRenameFtpFiles.getContentPane().add(txtFind, gbc_txtFind);
		txtFind.setColumns(10);

		lblErsetzen = new JLabel("Replace With:");
		GridBagConstraints gbc_lblErsetzen = new GridBagConstraints();
		gbc_lblErsetzen.anchor = GridBagConstraints.EAST;
		gbc_lblErsetzen.insets = new Insets(0, 0, 5, 5);
		gbc_lblErsetzen.gridx = 2;
		gbc_lblErsetzen.gridy = 1;
		frmRenameFtpFiles.getContentPane().add(lblErsetzen, gbc_lblErsetzen);

		txtReplace = new JTextField();
		txtReplace.setText("Replace");
		GridBagConstraints gbc_txtReplace = new GridBagConstraints();
		gbc_txtReplace.insets = new Insets(0, 0, 5, 0);
		gbc_txtReplace.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtReplace.gridx = 3;
		gbc_txtReplace.gridy = 1;
		frmRenameFtpFiles.getContentPane().add(txtReplace, gbc_txtReplace);
		txtReplace.setColumns(10);

		chckbxRenameFiles = new JCheckBox("Rename Files");
		chckbxRenameFiles.setSelected(true);
		GridBagConstraints gbc_chckbxRenameFiles = new GridBagConstraints();
		gbc_chckbxRenameFiles.anchor = GridBagConstraints.WEST;
		gbc_chckbxRenameFiles.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRenameFiles.gridx = 0;
		gbc_chckbxRenameFiles.gridy = 2;
		frmRenameFtpFiles.getContentPane().add(chckbxRenameFiles,
				gbc_chckbxRenameFiles);

		btnTestRenaming = new JButton("Test Renaming");
		btnTestRenaming.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!ftpServerRead) {

					if (checkFTP()) {

						readFTPServer();
						rename();

					} else {

						JOptionPane
								.showConfirmDialog(
										frmRenameFtpFiles,
										new JLabel(
												"FTP connection error. Please check your input."),
										"FTP Check", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					rename();
				}
			}
		});

		chckbxRenameDirectories = new JCheckBox("Rename Directories");
		GridBagConstraints gbc_chckbxRenameDirectories = new GridBagConstraints();
		gbc_chckbxRenameDirectories.anchor = GridBagConstraints.WEST;
		gbc_chckbxRenameDirectories.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRenameDirectories.gridx = 1;
		gbc_chckbxRenameDirectories.gridy = 2;
		frmRenameFtpFiles.getContentPane().add(chckbxRenameDirectories,
				gbc_chckbxRenameDirectories);
		GridBagConstraints gbc_btnTestRenaming = new GridBagConstraints();
		gbc_btnTestRenaming.fill = GridBagConstraints.BOTH;
		gbc_btnTestRenaming.gridwidth = 2;
		gbc_btnTestRenaming.insets = new Insets(0, 0, 0, 5);
		gbc_btnTestRenaming.gridx = 0;
		gbc_btnTestRenaming.gridy = 3;
		frmRenameFtpFiles.getContentPane().add(btnTestRenaming,
				gbc_btnTestRenaming);

		btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (checkFTP()) {

					if (!ftpServerRead) {
						readFTPServer();
						rename();
					}

					FTPClient ftp = new FTPClient();
					try {
						ftp.connect(txtHostaddress.getText(),
								Integer.parseInt(txtPort.getText()));
						ftp.login(txtName.getText(),
								new String(pwdPassword.getPassword()));
						ftp.changeWorkingDirectory(txtDir.getText());
						for (int i = 0; i < myModel.getRowCount(); i++) {

							if (myModel.getValueAt(i, 1).toString().length() > 0
									&& !myModel.getValueAt(i, 0).equals(
											myModel.getValueAt(i, 1))) {

								// rename this one!
								System.out.println("RENAME "
										+ myModel.getValueAt(i, 0).toString()
										+ " -> "
										+ myModel.getValueAt(i, 1).toString()
										+ " = "
										+ ftp.rename(myModel.getValueAt(i, 0)
												.toString(), myModel
												.getValueAt(i, 1).toString()));
							}
						}
						ftp.disconnect();
					} catch (IOException e1) {

						e1.printStackTrace();
					}

				} else {

					JOptionPane
							.showConfirmDialog(
									frmRenameFtpFiles,
									new JLabel(
											"FTP connection error. Please check your input."),
									"FTP Check", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		GridBagConstraints gbc_btnRename = new GridBagConstraints();
		gbc_btnRename.fill = GridBagConstraints.BOTH;
		gbc_btnRename.gridwidth = 2;
		gbc_btnRename.gridx = 2;
		gbc_btnRename.gridy = 3;
		frmRenameFtpFiles.getContentPane().add(btnRename, gbc_btnRename);
	}

	protected boolean checkFTP() {

		FTPClient tmpClient = new FTPClient();

		// format host address
		txtHostaddress.setText(UI_Utils.formatFTPHostAddress(txtHostaddress
				.getText().trim()));

		// check port
		try {
			Integer.parseInt(txtPort.getText());
		} catch (Exception e) {
			System.out.println("Port falsch");
			return false;
		}

		// check path
		txtDir.setText(UI_Utils.formatPathFTP(txtDir.getText().trim()));

		// check login
		try {
			tmpClient.connect(txtHostaddress.getText(),
					Integer.parseInt(txtPort.getText()));
			return tmpClient.login(txtName.getText(),
					new String(pwdPassword.getPassword()));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	protected void readFTPServer() {

		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(txtHostaddress.getText(),
					Integer.parseInt(txtPort.getText()));
			System.out.println("Log into ftp:" + ftp.login(txtName.getText(), new String(
					pwdPassword.getPassword())));
			FTPFile[] files = ftp.listFiles(txtDir.getText());
			ftp.disconnect();

			System.out.println(Arrays.toString(files));
			if (files != null) {

				myModel.setRowCount(0);
				for (FTPFile f : files) {

					if (f.isFile() && chckbxRenameFiles.isSelected()) {

						myModel.addRow(new String[] { f.getName(), "" });
					}
					if (f.isDirectory() && chckbxRenameDirectories.isSelected()) {

						myModel.addRow(new String[] { f.getName(), "" });
					}
				}
				ftpServerRead = true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void rename() {

		for (int i = 0; i < myModel.getRowCount(); i++) {

			String oldName = (String) myModel.getValueAt(i, 0);
			String newName = oldName.replace(txtFind.getText(),
					txtReplace.getText());
			myModel.setValueAt(newName, i, 1);
		}
	}
}
