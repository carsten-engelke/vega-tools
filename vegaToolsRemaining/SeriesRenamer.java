package org.vega.tools;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SeriesRenamer extends JFrame {

	/**
	 * Version 1.0 Basic funcionality
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtSeason;
	private JTextField txtEpisode;
	private File dir = new File(System.getProperty("user.dir"));
	private JTextField txtFileFilter;
	private DefaultListModel<File> fileModel = new DefaultListModel<File>();
	private JTextArea txtareaConsole = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SeriesRenamer frame = new SeriesRenamer();
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
	public SeriesRenamer() {
		final JFrame parent = this;
		setTitle("Series Renamer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JButton btnLoadFilesFrom = new JButton("Load Files from Directory");
		btnLoadFilesFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser openDir = new JFileChooser(dir);
				openDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				openDir.showOpenDialog(parent);
				dir = openDir.getSelectedFile();
				if (dir != null) {

					final String[] types = txtFileFilter.getText().split(";");
					File[] files = dir.listFiles(new FilenameFilter() {

						@Override
						public boolean accept(File dir, String name) {

							for (String type : types) {
								if (name.endsWith(type)) {
									return true;
								}
							}
							return false;
						}
					});
					fileModel.clear();
					for (File videoFile : files) {

						fileModel.addElement(videoFile);
					}
					txtName.setText(dir.getName());
					txtEpisode.setText("01");
					txtSeason.setText("01");
				}
			}
		});
		GridBagConstraints gbc_btnLoadFilesFrom = new GridBagConstraints();
		gbc_btnLoadFilesFrom.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoadFilesFrom.gridx = 0;
		gbc_btnLoadFilesFrom.gridy = 0;
		contentPane.add(btnLoadFilesFrom, gbc_btnLoadFilesFrom);

		JLabel lblTypes = new JLabel("Types:");
		GridBagConstraints gbc_lblTypes = new GridBagConstraints();
		gbc_lblTypes.anchor = GridBagConstraints.EAST;
		gbc_lblTypes.insets = new Insets(0, 0, 5, 5);
		gbc_lblTypes.gridx = 1;
		gbc_lblTypes.gridy = 0;
		contentPane.add(lblTypes, gbc_lblTypes);

		txtFileFilter = new JTextField();
		txtFileFilter.setText(".mkv;.mp4;.avi;.flv");
		GridBagConstraints gbc_txtFileFilter = new GridBagConstraints();
		gbc_txtFileFilter.insets = new Insets(0, 0, 5, 5);
		gbc_txtFileFilter.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFileFilter.gridx = 2;
		gbc_txtFileFilter.gridy = 0;
		contentPane.add(txtFileFilter, gbc_txtFileFilter);
		txtFileFilter.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		contentPane.add(scrollPane, gbc_scrollPane);

		final JList<File> list = new JList<File>();
		list.setModel(fileModel);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {

					File renameMe = list.getSelectedValue();
					File aim = new File(renameMe.getParent() + "/" + txtName.getText() + " - S" + txtSeason.getText()
							+ "E" + txtEpisode.getText()
							+ renameMe.getName().substring(renameMe.getName().lastIndexOf(".")));
					txtareaConsole.append("\n" + renameMe.getName() + " -> " + aim.getName() + "...");
					if (renameMe.renameTo(aim)) {

						int l = txtEpisode.getText().length();
						int num = Integer.parseInt(txtEpisode.getText()) + 1;
						txtEpisode.setText(String.format("%0" + l + "d", num));
						txtareaConsole.append("done");
						fileModel.remove(list.getSelectedIndex());
					} else {

						txtareaConsole.append("failed");
					}

				}
			}
		});
		scrollPane.setViewportView(list);

		JLabel lblName = new JLabel("Name:");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 1;
		contentPane.add(lblName, gbc_lblName);

		txtName = new JTextField();
		txtName.setText("Game of Simpsons");
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 2;
		gbc_txtName.gridy = 1;
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);

		JLabel lblSeason = new JLabel("Season:");
		GridBagConstraints gbc_lblSeason = new GridBagConstraints();
		gbc_lblSeason.anchor = GridBagConstraints.EAST;
		gbc_lblSeason.insets = new Insets(0, 0, 5, 5);
		gbc_lblSeason.gridx = 1;
		gbc_lblSeason.gridy = 2;
		contentPane.add(lblSeason, gbc_lblSeason);

		txtSeason = new JTextField();
		txtSeason.setToolTipText("Please adjust leading zeros according to season number!");
		txtSeason.setText("01");
		GridBagConstraints gbc_txtSeason = new GridBagConstraints();
		gbc_txtSeason.insets = new Insets(0, 0, 5, 5);
		gbc_txtSeason.anchor = GridBagConstraints.NORTH;
		gbc_txtSeason.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSeason.gridx = 2;
		gbc_txtSeason.gridy = 2;
		contentPane.add(txtSeason, gbc_txtSeason);
		txtSeason.setColumns(10);

		JButton btnSeasonUp = new JButton("+1");
		btnSeasonUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int l = txtSeason.getText().length();
				int num = Integer.parseInt(txtSeason.getText()) + 1;
				txtSeason.setText(String.format("%0" + l + "d", num));
				l = txtEpisode.getText().length();
				num = 1;
				txtEpisode.setText(String.format("%0" + l + "d", num));
			}
		});
		GridBagConstraints gbc_btnSeasonUp = new GridBagConstraints();
		gbc_btnSeasonUp.insets = new Insets(0, 0, 5, 0);
		gbc_btnSeasonUp.gridx = 3;
		gbc_btnSeasonUp.gridy = 2;
		contentPane.add(btnSeasonUp, gbc_btnSeasonUp);

		JLabel lblEpisode = new JLabel("Episode:");
		GridBagConstraints gbc_lblEpisode = new GridBagConstraints();
		gbc_lblEpisode.anchor = GridBagConstraints.EAST;
		gbc_lblEpisode.insets = new Insets(0, 0, 5, 5);
		gbc_lblEpisode.gridx = 1;
		gbc_lblEpisode.gridy = 3;
		contentPane.add(lblEpisode, gbc_lblEpisode);

		txtEpisode = new JTextField();
		txtEpisode.setToolTipText("Please adjust leading zeros according to episode number!");
		txtEpisode.setText("01");
		GridBagConstraints gbc_txtEpisode = new GridBagConstraints();
		gbc_txtEpisode.insets = new Insets(0, 0, 5, 5);
		gbc_txtEpisode.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEpisode.gridx = 2;
		gbc_txtEpisode.gridy = 3;
		contentPane.add(txtEpisode, gbc_txtEpisode);
		txtEpisode.setColumns(10);

		JLabel lblResult = new JLabel("Result:");
		GridBagConstraints gbc_lblResult = new GridBagConstraints();
		gbc_lblResult.insets = new Insets(0, 0, 5, 5);
		gbc_lblResult.gridx = 1;
		gbc_lblResult.gridy = 4;
		contentPane.add(lblResult, gbc_lblResult);

		JLabel lblFullName = new JLabel("Game of Simpsons - S01E01");
		lblFullName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblFullName = new GridBagConstraints();
		gbc_lblFullName.insets = new Insets(0, 0, 5, 0);
		gbc_lblFullName.anchor = GridBagConstraints.WEST;
		gbc_lblFullName.gridwidth = 2;
		gbc_lblFullName.gridx = 2;
		gbc_lblFullName.gridy = 4;
		contentPane.add(lblFullName, gbc_lblFullName);

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 5;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		txtareaConsole.setLineWrap(true);
		scrollPane_1.setViewportView(txtareaConsole);
	}

}
