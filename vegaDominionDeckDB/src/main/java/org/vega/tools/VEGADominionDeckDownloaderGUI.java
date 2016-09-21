package org.vega.tools;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class VEGADominionDeckDownloaderGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1117230535556348417L;
	private JPanel contentPane;
	private JTextField txtDominionDecksURL;
	private JTable table;
	private JButton btnSaveToFile;
	private JProgressBar progressBar;
	ArrayList<String> deckIDList = new ArrayList<String>();
	ArrayList<Deck> deckList = new ArrayList<Deck>();
	public final String[] setcodes = new String[] { "B001JQY6K4:base", "B00C9T4KM4:guilds", "B005JDFUOE:hinterlands",
			"B002THXRWK:seaside", "B008GRI010:darkages", "B002GJMOUC:intrigue", "B003YXZB22:prosperity",
			"B003D3OD4K:alchemy", "B004N3CRQA:cornucopia" };
	private JButton btnActualise;
	private JButton btnRemoveFromList;
	private JButton btnAddToList;
	private JScrollPane scrollPane;
	private File dbDir = new File(System.getProperty("user.dir"));
	private ArrayList<String[]> translate = new ArrayList<String[]>(); // translation
	private JButton btnTranslate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VEGADominionDeckDownloaderGUI frame = new VEGADominionDeckDownloaderGUI();
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
	public VEGADominionDeckDownloaderGUI() {
		final VEGADominionDeckDownloaderGUI p = this;
//		try {
//			setIconImage(Toolkit.getDefaultToolkit()
//					.getImage(VEGADominionDeckDownloaderGUI.class.getResource("/dominion-icon32x32.png")));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		setTitle("Dominion Deck Downloader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblDeckbuilderurl = new JLabel("Deckbuilder-URL:");
		GridBagConstraints gbc_lblDeckbuilderurl = new GridBagConstraints();
		gbc_lblDeckbuilderurl.anchor = GridBagConstraints.EAST;
		gbc_lblDeckbuilderurl.insets = new Insets(0, 0, 5, 5);
		gbc_lblDeckbuilderurl.gridx = 0;
		gbc_lblDeckbuilderurl.gridy = 0;
		contentPane.add(lblDeckbuilderurl, gbc_lblDeckbuilderurl);

		txtDominionDecksURL = new JTextField();
		txtDominionDecksURL.setText("http://www.dominiondeck.com");
		GridBagConstraints gbc_txtHttpwwwdominiondeckscom = new GridBagConstraints();
		gbc_txtHttpwwwdominiondeckscom.insets = new Insets(0, 0, 5, 5);
		gbc_txtHttpwwwdominiondeckscom.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHttpwwwdominiondeckscom.gridx = 1;
		gbc_txtHttpwwwdominiondeckscom.gridy = 0;
		contentPane.add(txtDominionDecksURL, gbc_txtHttpwwwdominiondeckscom);
		txtDominionDecksURL.setColumns(10);

		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (JOptionPane.showConfirmDialog(VEGADominionDeckDownloaderGUI.this,
						"Discard all Decks and re-download complete database?") == JOptionPane.YES_OPTION) {
					clearDeckDB();
					new DownloadThread(p, 500, 75).start();
				}
			}
		});
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDownload.insets = new Insets(0, 0, 5, 0);
		gbc_btnDownload.gridx = 2;
		gbc_btnDownload.gridy = 0;
		contentPane.add(btnDownload, gbc_btnDownload);

		JButton btnLoadFromFile = new JButton("Load from file");
		btnLoadFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser(dbDir);
				if (jfc.showOpenDialog(VEGADominionDeckDownloaderGUI.this) == JFileChooser.APPROVE_OPTION) {

					File db = jfc.getSelectedFile();
					dbDir = db.getParentFile();
					try {
						BufferedReader br = new BufferedReader(new FileReader(db));
						String line = br.readLine();
						// clear current list
						clearDeckDB();
						while (line != null && line != "") {

							Deck d = createDeckFromString(line);
							if (d != null) {

								addDeck(d);
							}
							line = br.readLine();
						}
						br.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		GridBagConstraints gbc_btnLoadFromFile = new GridBagConstraints();
		gbc_btnLoadFromFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadFromFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadFromFile.gridx = 2;
		gbc_btnLoadFromFile.gridy = 1;
		contentPane.add(btnLoadFromFile, gbc_btnLoadFromFile);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridheight = 7;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(table);
		scrollPane.setEnabled(false);
		contentPane.add(scrollPane, gbc_scrollPane);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Name", "Constructed", "Rating", "Votes", "Played", "Cards", "Sets" }) {

			/**
			 * non-editable table
			 */
			private static final long serialVersionUID = 6031922121724443107L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		btnActualise = new JButton();
		btnActualise.setText("Refresh List");
		btnActualise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DefaultTableModel model = ((DefaultTableModel) table.getModel());
				model.removeRow(0);
				for (Deck d : deckList) {

					model.addRow(new Object[] { d.id, d.name, d.constructed, d.rating, d.votes, d.playnum,
							String.join(" , ", d.cards), String.join(" , ", d.sets) });
				}
			}
		});

		btnSaveToFile = new JButton("Save to file");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfcSaveDB = new JFileChooser(dbDir);
				if (jfcSaveDB.showSaveDialog(VEGADominionDeckDownloaderGUI.this) == JFileChooser.APPROVE_OPTION) {

					ArrayList<Deck> saveList = new ArrayList<Deck>();
					for (int i = 0; i < table.getRowCount(); i++) {

						String id = (String) table.getValueAt(i, 0);
						for (Deck d : deckList) {

							if (d.id.equals(id)) {
								saveList.add(d);
								break;
							}
						}
					}
					deckList = saveList;
					saveDecksToFile(saveList, jfcSaveDB.getSelectedFile());
				}
			}
		});
		GridBagConstraints gbc_btnSaveToFile = new GridBagConstraints();
		gbc_btnSaveToFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveToFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaveToFile.gridx = 2;
		gbc_btnSaveToFile.gridy = 2;
		contentPane.add(btnSaveToFile, gbc_btnSaveToFile);
		GridBagConstraints gbc_btnActualise = new GridBagConstraints();
		gbc_btnActualise.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnActualise.insets = new Insets(0, 0, 5, 0);
		gbc_btnActualise.gridx = 2;
		gbc_btnActualise.gridy = 3;
		contentPane.add(btnActualise, gbc_btnActualise);

		btnRemoveFromList = new JButton("Remove from list");
		btnRemoveFromList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (table.getSelectedRow() != -1) {

					DefaultTableModel model = (DefaultTableModel) table.getModel();
					removeDeck((String) model.getValueAt(table.getSelectedRow(), 0));
				}
			}
		});
		GridBagConstraints gbc_btnRemoveFromList = new GridBagConstraints();
		gbc_btnRemoveFromList.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveFromList.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemoveFromList.gridx = 2;
		gbc_btnRemoveFromList.gridy = 4;
		contentPane.add(btnRemoveFromList, gbc_btnRemoveFromList);

		btnAddToList = new JButton("Add to list");
		GridBagConstraints gbc_btnAddToList = new GridBagConstraints();
		gbc_btnAddToList.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddToList.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddToList.gridx = 2;
		gbc_btnAddToList.gridy = 5;
		contentPane.add(btnAddToList, gbc_btnAddToList);

		btnTranslate = new JButton("Auf Deutsch");
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				new Thread() {

					public void run() {

						progressBar.setMaximum(table.getRowCount());
						for (int i = 0; i < table.getRowCount(); i++) {

							String s = (String) table.getValueAt(i, 6);
							for (String[] t : translate) {
								s = s.replace(t[0].toLowerCase(), t[1]);
							}
							table.setValueAt(s, i, 6);
							s = (String) table.getValueAt(i, 7);
							for (String[] t : translate) {
								s = s.replace(t[0].toLowerCase(), t[1]);
							}
							table.setValueAt(s, i, 7);
							progressBar.setValue(i + 1);
						}
						ArrayList<Deck> deckLIstTrans = new ArrayList<Deck>();
						progressBar.setMaximum(deckList.size());
						int i = 1;
						for (Deck d : deckList) {
							ArrayList<String> c = d.cards;
							ArrayList<String> cTrans = new ArrayList<String>();
							for (String card : c) {

								for (String[] t : translate) {
									card = card.replace(t[0].toLowerCase(), t[1]);
								}
								cTrans.add(card);
							}
							ArrayList<String> s = d.sets;
							ArrayList<String> sTrans = new ArrayList<String>();
							for (String set : s) {

								for (String[] t : translate) {
									set = set.replace(t[0].toLowerCase(), t[1]);
								}
								sTrans.add(set);
							}
							deckLIstTrans.add(new Deck(d.id, d.name, d.constructed, d.rating, d.votes, d.playnum,
									cTrans, sTrans));
							progressBar.setValue(i);
							i++;
						}
						deckList = deckLIstTrans;
						System.out.println(deckList.get(10));
					};
				}.start();
			}
		});
		GridBagConstraints gbc_btnTranslate = new GridBagConstraints();
		gbc_btnTranslate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTranslate.insets = new Insets(0, 0, 5, 0);
		gbc_btnTranslate.gridx = 2;
		gbc_btnTranslate.gridy = 6;
		contentPane.add(btnTranslate, gbc_btnTranslate);

		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 3;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 8;
		contentPane.add(progressBar, gbc_progressBar);

		// load translations & main.db
		new Thread() {

			public void run() {

				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(VEGADominionDeckDownloaderGUI.class
							.getResource("/en-de.txt").openStream()));
					String t = br.readLine();
					while (t != null && t != "") {

						translate.add(t.split("\t"));
						t = br.readLine();
					}
					br.close();
					br = new BufferedReader(new FileReader("main.db"));
					t = br.readLine();
					while (t != null) {

						Deck d = createDeckFromString(t);
						if (d != null) {

							addDeck(d);
						}
						t = br.readLine();
					}
					br.close();
				} catch (Exception ignore) {
				}
			};
		}.start();
	}

	protected void clearDeckDB() {

		((DefaultTableModel) table.getModel()).setRowCount(0);
		deckIDList.clear();
		deckList.clear();
	}

	class DownloadThread extends Thread {

		VEGADominionDeckDownloaderGUI parent;
		int tmpListSize;
		int doubleDeckMax;

		public DownloadThread(VEGADominionDeckDownloaderGUI parent, int tmpListSize, int doubleDeckMax) {

			this.parent = parent;
			this.tmpListSize = tmpListSize;
			this.doubleDeckMax = doubleDeckMax;
		}

		@Override
		public void run() {

			String url = parent.txtDominionDecksURL.getText();
			boolean loadNextList = true; // is set to false if a deck is found
											// that is already in the database
											// AND the number of found decks has
											// reached the number of stored
											// decks on the website
			int listNo = 0; // page number in URL (each page contains ~20 decks)
			int deckNo = 0;
			int maxDeckNo = 0;
			ArrayList<Deck> tmpdeckList = new ArrayList<Deck>();
			int tmpFileNo = 0;
			int doubleDecks = 0;

			while (loadNextList) {

				try {
					URL listURL = new URL(url + "/games?page=" + listNo);
					// System.out.println("list: " + url + " > " + listURL);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(listURL.openStream(), Charset.forName("UTF-8")));
					String line = br.readLine();
					// class="feed-icon" is first mentioned after the deck list
					while (line != null && !line.contains("class=\"feed-icon\"")) {

						if (maxDeckNo == 0 && line.contains("Listing <em>")) {

							// max deck number detected -> set in order to read
							// further list pages
							line = line.substring(line.indexOf("Listing <em>") + 12);
							maxDeckNo = Integer.parseInt(line.substring(0, line.indexOf("</em>")));
							System.out.println("deck max no set to: " + maxDeckNo);
							parent.progressBar.setMaximum(maxDeckNo);
						}
						if (line.contains("field-content\"><a href=\"/games/")) {

							// new deck detected -> read it if not yet in
							// database
							line = line.substring(line.indexOf("\">") + 2);
							line = line.substring(line.indexOf("=\"") + 2);
							String deckID = line.substring(0, line.indexOf("\">"));
							line = line.substring(line.indexOf("\">") + 2);
							String deckName = line.substring(0, line.indexOf("</a")).replace(";", " ");
							deckName = deckName.replace("&#039 ", "'");

							if (!deckIDList.contains(deckID)) {

								deckNo++;
								URL deckURL = new URL(url + deckID);
								BufferedReader br1 = new BufferedReader(new InputStreamReader(deckURL.openStream()));
								String line1 = br1.readLine();
								ArrayList<String> cards = new ArrayList<String>();
								ArrayList<String> sets = new ArrayList<String>();
								boolean constructed = false;
								float vote = 0;
								int votenum = 0;
								int playnum = 0;
								while (line1 != null) {

									if (line1.contains("card-name\"><a href=\"/cards")) {

										line1 = line1.substring(line1.indexOf("card-name\"><a href=\"/cards/") + 27);
										cards.add(line1.substring(0, line1.indexOf("\">")));
									}
									if (line1.contains("This game was <em class=\"")) {

										line1 = line1.substring(line1.indexOf("class") + 7);
										if (line1.startsWith("custom")) {

											constructed = true;
										}
									}
									if (line1.contains("class=\"flag-count\">")) {

										line1 = line1.substring(line1.indexOf("class=\"flag-count\">") + 26);
										playnum = Integer.parseInt(line1.substring(0, line1.indexOf(" ")));
									}
									if (line1.contains("class=\"average-rating\">Average: <span>")) {

										line1 = line1.substring(
												line1.indexOf("class=\"average-rating\">Average: <span>") + 38);
										vote = Float.parseFloat(line1.substring(0, line1.indexOf("</span")));
										line1 = line1.substring(line1.indexOf("class=\"total-votes\">(<span>") + 27);
										votenum = Integer.parseInt(line1.substring(0, line1.indexOf("</span")));
									}
									if (line1.contains("http://www.amazon.com")) {

										line1 = line1
												.substring(line1.indexOf("http://www.amazon.com/gp/product/") + 33);
										String code = line1.substring(0, line1.indexOf("/"));
										boolean foundset = false;
										for (String s : setcodes) {

											String[] setcode = s.split(":");
											if (setcode[0].equals(code)) {
												sets.add(setcode[1]);
												foundset = true;
												break;
											}
										}
										if (!foundset) {
											System.out.println("Set not found:" + code);
											sets.add(code);
										}
									}
									line1 = br1.readLine();
								}

								br1.close();
								parent.progressBar.setValue(deckNo);
								parent.progressBar.setString(deckNo * 100 / maxDeckNo + "% - " + deckID);
								parent.progressBar.setStringPainted(true);
								Deck d = new Deck(deckID, deckName, constructed, vote, votenum, playnum, cards, sets);
								tmpdeckList.add(d);
								addDeck(d);

								if (tmpdeckList.size() >= tmpListSize) {

									saveDecksToFile(tmpdeckList, new File("tmpDB-" + tmpFileNo + ".db"));
									tmpFileNo++;
									tmpdeckList.clear();
								}
							} else {

								// deckID already in db -> check if deck number
								// exceeds max deck number or double deck number
								// exceeds max double deck number
								if (deckNo >= maxDeckNo || doubleDecks >= doubleDeckMax) {
									loadNextList = false;
								}
								doubleDecks++;
							}
						}
						line = br.readLine();
					}
					br.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				listNo++;
			}
			saveDecksToFile(tmpdeckList, new File("tmpDB-" + tmpFileNo + ".db"));
			tmpFileNo++;
			tmpdeckList.clear();
			parent.progressBar.setValue(maxDeckNo);
			parent.progressBar.setString("100% - done");
		}
	}

	public boolean addDeck(Deck d) {

		if (!deckIDList.contains(d.id)) {

			deckIDList.add(d.id);
			deckList.add(d);
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.addRow(new Object[] { d.id, d.name, d.constructed, d.rating, d.votes, d.playnum,
					String.join(" , ", d.cards), String.join(" , ", d.sets) });
			return true;
		}
		return false;
	}

	public boolean removeDeck(String id) {

		if (deckIDList.contains(id)) {

			boolean foundDeck = false;
			for (Deck d : deckList) {

				if (d.id.equals(id)) {

					if (deckList.remove(d)) {

						foundDeck = true;
						break;
					}
				}
			}
			if (foundDeck) {

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				for (int i = 0; i < model.getRowCount(); i++) {

					if (model.getValueAt(i, 0).toString().equals(id)) {

						model.removeRow(i);
						return deckIDList.remove(id);
					}
				}
			}
		}
		return false;
	}

	public static boolean saveDecksToFile(ArrayList<Deck> deckList, File csvTableFile) {

		try {
			FileWriter fw = new FileWriter(csvTableFile);
			for (Deck d : deckList) {

				fw.write(d.toString() + "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Deck createDeckFromString(String s) {

		try {
			String[] strs = s.split(";");
			strs[6] = strs[6].substring(1, strs[6].length() - 2);
			ArrayList<String> cards = new ArrayList<String>(Arrays.asList(strs[6].split(" , ")));
			strs[7] = strs[7].substring(1, strs[7].length() - 2);
			ArrayList<String> sets;
			if (strs[7].contains(" , ")) {

				sets = new ArrayList<String>(Arrays.asList(strs[7].split(" , ")));
			} else {

				sets = new ArrayList<String>();
				sets.add(strs[7]);
			}
			return new Deck(strs[0], strs[1], Boolean.parseBoolean(strs[2]), Float.parseFloat(strs[3]),
					Integer.parseInt(strs[4]), Integer.parseInt(strs[5]), cards, sets);
		} catch (Exception e) {
			return null;
		}
	}

	class Deck {

		public String id;
		public String name;
		public boolean constructed;
		public float rating;
		public int votes;
		public int playnum;
		public ArrayList<String> cards = new ArrayList<String>();
		public ArrayList<String> sets = new ArrayList<String>();

		public Deck(String id, String name, boolean contructed, float rating, int votes, int playnum,
				ArrayList<String> cards, ArrayList<String> sets) {

			this.id = id;
			this.name = name;
			this.constructed = contructed;
			this.rating = rating;
			this.votes = votes;
			this.playnum = playnum;
			this.cards = cards;
			this.sets = sets;
		}

		public String toString() {

			return id + ";" + name + ";" + constructed + ";" + rating + ";" + votes + ";" + playnum + ";[ "
					+ String.join(" , ", cards) + " ];[ " + String.join(" , ", sets) + " ]";
		}
	}
}
