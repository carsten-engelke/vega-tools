package org.vega.tools;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class VEGAMKVExtractGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMkvectractpath;
	private JTextField txtFilepath;
	private JTextField txtIndex;
	JTextArea txtrOutput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VEGAMKVExtractGUI frame = new VEGAMKVExtractGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VEGAMKVExtractGUI() {
		setTitle("Extract MKV GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblMkvExtractPath = new JLabel("MKV Extract Path:");
		GridBagConstraints gbc_lblMkvExtractPath = new GridBagConstraints();
		gbc_lblMkvExtractPath.insets = new Insets(0, 0, 5, 5);
		gbc_lblMkvExtractPath.anchor = GridBagConstraints.EAST;
		gbc_lblMkvExtractPath.gridx = 0;
		gbc_lblMkvExtractPath.gridy = 0;
		contentPane.add(lblMkvExtractPath, gbc_lblMkvExtractPath);

		txtMkvectractpath = new JTextField();
		txtMkvectractpath.setText("C:\\Program Files\\MKVToolNix\\mkvextract.exe");
		GridBagConstraints gbc_txtMkvectractpath = new GridBagConstraints();
		gbc_txtMkvectractpath.gridwidth = 2;
		gbc_txtMkvectractpath.insets = new Insets(0, 0, 5, 5);
		gbc_txtMkvectractpath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMkvectractpath.gridx = 1;
		gbc_txtMkvectractpath.gridy = 0;
		contentPane.add(txtMkvectractpath, gbc_txtMkvectractpath);
		txtMkvectractpath.setColumns(10);

		JButton btnSelectMkvextractexe = new JButton("Select mkvextract.exe");
		btnSelectMkvextractexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				if (jfc.showOpenDialog(VEGAMKVExtractGUI.this) == JFileChooser.APPROVE_OPTION) {

					File mkvextract = jfc.getSelectedFile();
					if (mkvextract.exists() && mkvextract.getName().equals("mkvextract.exe")) {

						txtMkvectractpath.setText(mkvextract.getAbsolutePath());
					} else {

						txtrOutput.append("\nMKVextract not found!");
					}
				}

			}
		});
		GridBagConstraints gbc_btnSelectMkvextractexe = new GridBagConstraints();
		gbc_btnSelectMkvextractexe.gridwidth = 2;
		gbc_btnSelectMkvextractexe.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelectMkvextractexe.gridx = 3;
		gbc_btnSelectMkvextractexe.gridy = 0;
		contentPane.add(btnSelectMkvextractexe, gbc_btnSelectMkvextractexe);

		JLabel lblFilepath = new JLabel("FilePath:");
		GridBagConstraints gbc_lblFilepath = new GridBagConstraints();
		gbc_lblFilepath.anchor = GridBagConstraints.EAST;
		gbc_lblFilepath.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilepath.gridx = 0;
		gbc_lblFilepath.gridy = 1;
		contentPane.add(lblFilepath, gbc_lblFilepath);

		txtFilepath = new JTextField();
		txtFilepath.setText("D:\\Downloads");
		GridBagConstraints gbc_txtFilepath = new GridBagConstraints();
		gbc_txtFilepath.gridwidth = 2;
		gbc_txtFilepath.insets = new Insets(0, 0, 5, 5);
		gbc_txtFilepath.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFilepath.gridx = 1;
		gbc_txtFilepath.gridy = 1;
		contentPane.add(txtFilepath, gbc_txtFilepath);
		txtFilepath.setColumns(10);

		JButton btnShowIndices = new JButton("Show Indices");
		btnShowIndices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				File mkv = new File(txtMkvectractpath.getText());
				File f = new File(txtFilepath.getText());
				if (mkv.exists() && mkv.getName().equals("mkvextract.exe") && f.exists()) {

					try {
						Process p = new ProcessBuilder(mkv.getParent() + "/mkvmerge.exe", "-i", f.getAbsolutePath())
								.start();
						BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String s = br.readLine();
						while (s != null) {

							txtrOutput.append("\n" + s);
							s = br.readLine();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		GridBagConstraints gbc_btnShowIndices = new GridBagConstraints();
		gbc_btnShowIndices.insets = new Insets(0, 0, 5, 5);
		gbc_btnShowIndices.gridx = 3;
		gbc_btnShowIndices.gridy = 1;
		contentPane.add(btnShowIndices, gbc_btnShowIndices);

		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser jfc = new JFileChooser(txtFilepath.getText());
				if (jfc.showOpenDialog(VEGAMKVExtractGUI.this) == JFileChooser.APPROVE_OPTION) {

					File f = jfc.getSelectedFile();
					if (f.exists()) {

						txtFilepath.setText(f.getAbsolutePath());
					} else {

						txtrOutput.append("\nFile not found");
					}
				}
			}
		});
		GridBagConstraints gbc_btnSelectFile = new GridBagConstraints();
		gbc_btnSelectFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelectFile.gridx = 4;
		gbc_btnSelectFile.gridy = 1;
		contentPane.add(btnSelectFile, gbc_btnSelectFile);

		JLabel lblIndex = new JLabel("Index:");
		GridBagConstraints gbc_lblIndex = new GridBagConstraints();
		gbc_lblIndex.insets = new Insets(0, 0, 5, 5);
		gbc_lblIndex.anchor = GridBagConstraints.EAST;
		gbc_lblIndex.gridx = 0;
		gbc_lblIndex.gridy = 2;
		contentPane.add(lblIndex, gbc_lblIndex);

		txtIndex = new JTextField();
		txtIndex.setText("1");
		GridBagConstraints gbc_txtIndex = new GridBagConstraints();
		gbc_txtIndex.gridwidth = 2;
		gbc_txtIndex.insets = new Insets(0, 0, 5, 5);
		gbc_txtIndex.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIndex.gridx = 1;
		gbc_txtIndex.gridy = 2;
		contentPane.add(txtIndex, gbc_txtIndex);
		txtIndex.setColumns(10);

		JButton btnExtractIndex = new JButton("Extract Index");
		btnExtractIndex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				File mkv = new File(txtMkvectractpath.getText());
				File f = new File(txtFilepath.getText());
				if (mkv.exists() && mkv.getName().equals("mkvextract.exe") && f.exists()) {

					try {
						Process p = new ProcessBuilder(mkv.getParent() + "/mkvextract.exe", "tracks",
								f.getAbsolutePath(),
								txtIndex.getText() + ":"
										+ f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(".") - 1) + ".audio")
												.start();
						BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String s = br.readLine();
						while (s != null) {

							txtrOutput.append("\n" + s);
							s = br.readLine();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		GridBagConstraints gbc_btnExtractIndex = new GridBagConstraints();
		gbc_btnExtractIndex.gridwidth = 2;
		gbc_btnExtractIndex.insets = new Insets(0, 0, 5, 0);
		gbc_btnExtractIndex.gridx = 3;
		gbc_btnExtractIndex.gridy = 2;
		contentPane.add(btnExtractIndex, gbc_btnExtractIndex);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);

		txtrOutput = new JTextArea();
		scrollPane.setViewportView(txtrOutput);
		txtrOutput.setText("Output");

		File mkvextract = new File("mkvextract.exe");
		if (mkvextract.exists()) {

			txtMkvectractpath.setText(mkvextract.getAbsolutePath());
		}
	}

}
