package org.vega.tools;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.io.FileUtils;

public class GUI_URL_Downloader {

	private JFrame frmUrlDownloader;
	private JTextField txtInput;
	private JTextArea areaResultURLs;

	int inputCounter = 0;
	private JTextField txtDLDir;
	private JProgressBar progressBar;

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
					GUI_URL_Downloader window = new GUI_URL_Downloader();
					window.frmUrlDownloader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_URL_Downloader() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUrlDownloader = new JFrame();
		frmUrlDownloader.setTitle("URL Downloader");
		frmUrlDownloader.setBounds(100, 100, 450, 300);
		frmUrlDownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		frmUrlDownloader.getContentPane().setLayout(gridBagLayout);

		JLabel lblUrlinput = new JLabel("URL-Input:");
		GridBagConstraints gbc_lblUrlinput = new GridBagConstraints();
		gbc_lblUrlinput.insets = new Insets(0, 0, 5, 5);
		gbc_lblUrlinput.anchor = GridBagConstraints.EAST;
		gbc_lblUrlinput.gridx = 0;
		gbc_lblUrlinput.gridy = 0;
		frmUrlDownloader.getContentPane().add(lblUrlinput, gbc_lblUrlinput);

		txtInput = new JTextField();
		txtInput.setToolTipText("URL - eg: http://something[01-14].com/[firstInst;secondInst]/image.jpg [001-5]->defines leading zeroes, [hi;you;there]->defines string array [a-o]->defines char array");
		txtInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {

				inputCounter++;
				new UpdateThread(inputCounter, 3000L).start();
			}
		});
		txtInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				updateURLs();
			}
		});
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		frmUrlDownloader.getContentPane().add(txtInput, gbc_textField);
		txtInput.setColumns(10);

		final JButton btnUpdateInput = new JButton("");
		btnUpdateInput.setIcon(new ImageIcon(GUI_URL_Downloader.class
				.getResource("/org/vega/tools/run-rebuild.png")));
		GridBagConstraints gbc_btnUpdateInput = new GridBagConstraints();
		gbc_btnUpdateInput.insets = new Insets(0, 0, 5, 0);
		gbc_btnUpdateInput.gridx = 2;
		gbc_btnUpdateInput.gridy = 0;
		frmUrlDownloader.getContentPane().add(btnUpdateInput, gbc_btnUpdateInput);

		JLabel lblDownloaddirectory = new JLabel("Download-Directory:");
		GridBagConstraints gbc_lblDownloaddirectory = new GridBagConstraints();
		gbc_lblDownloaddirectory.anchor = GridBagConstraints.EAST;
		gbc_lblDownloaddirectory.insets = new Insets(0, 0, 5, 5);
		gbc_lblDownloaddirectory.gridx = 0;
		gbc_lblDownloaddirectory.gridy = 1;
		frmUrlDownloader.getContentPane().add(lblDownloaddirectory,
				gbc_lblDownloaddirectory);

		txtDLDir = new JTextField();
		GridBagConstraints gbc_txtDLDir = new GridBagConstraints();
		gbc_txtDLDir.insets = new Insets(0, 0, 5, 5);
		gbc_txtDLDir.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDLDir.gridx = 1;
		gbc_txtDLDir.gridy = 1;
		frmUrlDownloader.getContentPane().add(txtDLDir, gbc_txtDLDir);
		txtDLDir.setColumns(10);

		final JButton btnSelectDirectory = new JButton("Select Directory");
		btnSelectDirectory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser selectDirChooser = new JFileChooser(txtDLDir.getText());
				selectDirChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (selectDirChooser.showOpenDialog(frmUrlDownloader) == JFileChooser.APPROVE_OPTION) {

					txtDLDir.setText(selectDirChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		});
		GridBagConstraints gbc_btnSelectDirectory = new GridBagConstraints();
		gbc_btnSelectDirectory.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelectDirectory.gridx = 2;
		gbc_btnSelectDirectory.gridy = 1;
		frmUrlDownloader.getContentPane().add(btnSelectDirectory,
				gbc_btnSelectDirectory);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		frmUrlDownloader.getContentPane().add(scrollPane, gbc_scrollPane);

		areaResultURLs = new JTextArea();
		areaResultURLs.setEditable(false);
		scrollPane.setViewportView(areaResultURLs);

		final JButton btnStartDownload = new JButton("Start Download");
		btnStartDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new DownloadThread(areaResultURLs.getText().split("\n"), new DownloadListener() {

					@Override
					public void progressChanged(int min, int max, int actual) {

						progressBar.setMinimum(min);
						progressBar.setMaximum(max);
						progressBar.setValue(actual);
					}

					@Override
					public void downloadCompleted() {

						progressBar.setMinimum(0);
						progressBar.setMaximum(10);
						progressBar.setValue(10);
						txtDLDir.setEnabled(true);
						txtInput.setEnabled(true);
						btnSelectDirectory.setEnabled(true);
						btnStartDownload.setEnabled(true);
						btnUpdateInput.setEnabled(true);
					}
					
				}).start();
				
				txtDLDir.setEnabled(false);
				txtInput.setEnabled(false);
				btnSelectDirectory.setEnabled(false);
				btnStartDownload.setEnabled(false);
				btnUpdateInput.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnStartDownload = new GridBagConstraints();
		gbc_btnStartDownload.gridwidth = 3;
		gbc_btnStartDownload.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartDownload.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartDownload.gridx = 0;
		gbc_btnStartDownload.gridy = 3;
		frmUrlDownloader.getContentPane().add(btnStartDownload,
				gbc_btnStartDownload);

		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 3;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 4;
		frmUrlDownloader.getContentPane().add(progressBar, gbc_progressBar);
	}

	private void updateURLs() {

		String s = txtInput.getText();
		s = getDecentURL(s);
		txtInput.setText(s);

		ArrayList<String> stringList = new ArrayList<String>();

		addNextMultiplier(s, stringList);

		String whole = "";
		for (String st : stringList) {

			whole += st + "\n";
		}

		areaResultURLs.setText(whole);
	}

	private void addNextMultiplier(String input, ArrayList<String> urls) {

		if (input.contains("[") && input.contains("]")
				&& input.indexOf("[") < input.indexOf("]")) {

			String before = input.substring(0, input.indexOf("["));
			String after = input.substring(input.indexOf("]") + 1);
			String mult = input.substring(input.indexOf("[") + 1,
					input.indexOf("]")).trim();

			if (mult.contains(";")) {

				// list of strings

				String[] subs = mult.split(";");
				for (String s : subs) {

					if (s.contains("\"")) {
						s = s.substring(s.indexOf("\"") + 1);
						s = s.substring(0, s.indexOf("\""));
					} else {

						s = s.trim();
					}
					addNextMultiplier(before + s + after, urls);
				}
			} else {
				if (mult.contains("-")) {

					// range of numbers or chars

					String first = mult.split("-")[0].trim();
					String second = mult.split("-")[1].trim();

					if (isInteger(first) && isInteger(second)) {

						// range of numbers

						// check leading zeroes

						boolean leadZero = false;
						int lower = Integer.parseInt(first);
						int upper = Integer.parseInt(second);
						if (lower > upper) {

							lower = upper;
							upper = Integer.parseInt(first);
							first = second;
						}
						if (first.startsWith("0")) {

							leadZero = true;
						}
						for (int i = lower; i <= upper; i++) {

							if (leadZero) {

								addNextMultiplier(
										before
												+ String.format(
														"%0" + first.length()
																+ "d", i)
												+ after, urls);
							} else {

								addNextMultiplier(before + i + after, urls);
							}
						}
					} else {

						if (first.length() == 1 && second.length() == 1) {

							// range of chars
							char lower = first.charAt(0);
							char upper = second.charAt(0);

							if (lower > upper) {

								lower = upper;
								upper = first.charAt(0);
							}

							for (char c = lower; c <= upper; c++) {

								addNextMultiplier(before + c + after, urls);
							}
						}
					}
				}
			}
		} else {

			urls.add(input);
		}
	}

	private String getDecentURL(String input) {

		String s = input.replace('\\', '/');
		if (!s.startsWith("http://")) {

			if (s.contains("://")) {
				s = s.substring(s.indexOf("://" + 3));
			}

			s = "http://" + s;
		}

		while (s.endsWith("/")) {
			s = s.substring(0, s.length() - 1);
		}

		return s;
	}

	public static boolean isInteger(String str) {

		// from:
		// http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java
		// Jonas Klemming

		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}

	public class UpdateThread extends Thread {

		int myCounter;
		long waitTimeMillis;

		public UpdateThread(int myCounter, long waitTimeMillis) {

			this.myCounter = myCounter;
			this.waitTimeMillis = waitTimeMillis;
		}

		@Override
		public void run() {

			try {
				sleep(waitTimeMillis);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

			if (inputCounter == myCounter) {

				updateURLs();
			}
		}
	}

	public class DownloadThread extends Thread {

		private String[] urls;
		private DownloadListener l;

		public DownloadThread(String[] urls, DownloadListener l) {

			this.urls = urls;
			this.l = l;
		}

		@Override
		public void run() {

			File dlDir = new File(txtDLDir.getText().trim());
			if (!dlDir.exists()) {
				dlDir.mkdirs();
			}
			
			int i = 0;
			l.progressChanged(0, urls.length, i);
			for (String url : urls) {

				try {
					File destination = new File(txtDLDir.getText().trim()
							+ "/" + url.substring(url.lastIndexOf("/")));
					if (!destination.exists() || destination.length() == 0) {
						FileUtils.copyURLToFile(new URL(url), destination);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				i++;
				l.progressChanged(0, urls.length, i);
			}
			
			l.downloadCompleted();
		}
	}

	public interface DownloadListener {

		public void progressChanged(int min, int max, int actual);

		public void downloadCompleted();
	}
}
