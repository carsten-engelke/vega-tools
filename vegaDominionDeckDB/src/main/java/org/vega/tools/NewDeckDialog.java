package org.vega.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NewDeckDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 724020134773393430L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNewdeck;
	private JTextField txtName;
	private JTextField txtVote;
	private JTextField txtTotalVotes;
	private JTextField txtPlayed;
	private JTextField txtCards;
	private JTextField txtSets;
	private JCheckBox chckbxConstructed;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewDeckDialog dialog = new NewDeckDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewDeckDialog(final VEGADominionDeckDownloaderGUI parent) {
		setTitle("Add new Deck");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblDeckid = new JLabel("DeckID:");
			GridBagConstraints gbc_lblDeckid = new GridBagConstraints();
			gbc_lblDeckid.insets = new Insets(0, 0, 5, 5);
			gbc_lblDeckid.anchor = GridBagConstraints.EAST;
			gbc_lblDeckid.gridx = 0;
			gbc_lblDeckid.gridy = 0;
			contentPanel.add(lblDeckid, gbc_lblDeckid);
		}
		{
			txtNewdeck = new JTextField();
			txtNewdeck.setText("newDeck");
			GridBagConstraints gbc_txtNewdeck = new GridBagConstraints();
			gbc_txtNewdeck.insets = new Insets(0, 0, 5, 5);
			gbc_txtNewdeck.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtNewdeck.gridx = 1;
			gbc_txtNewdeck.gridy = 0;
			contentPanel.add(txtNewdeck, gbc_txtNewdeck);
			txtNewdeck.setColumns(10);
		}
		{
			JButton btnCheckAvailability = new JButton("Check Availability");
			btnCheckAvailability.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					if (parent != null) {

						if (!parent.deckIDList.contains(txtNewdeck.getText())) {

							JOptionPane.showMessageDialog((Component) arg0.getSource(), "ID available!");
						} else {

							JOptionPane.showMessageDialog((Component) arg0.getSource(), "ID available!");
						}
					}
				}
			});
			GridBagConstraints gbc_btnCheckAvailability = new GridBagConstraints();
			gbc_btnCheckAvailability.insets = new Insets(0, 0, 5, 5);
			gbc_btnCheckAvailability.gridx = 2;
			gbc_btnCheckAvailability.gridy = 0;
			contentPanel.add(btnCheckAvailability, gbc_btnCheckAvailability);
		}
		{
			JLabel lblName = new JLabel("Name:");
			GridBagConstraints gbc_lblName = new GridBagConstraints();
			gbc_lblName.anchor = GridBagConstraints.EAST;
			gbc_lblName.insets = new Insets(0, 0, 5, 5);
			gbc_lblName.gridx = 0;
			gbc_lblName.gridy = 1;
			contentPanel.add(lblName, gbc_lblName);
		}
		{
			txtName = new JTextField();
			txtName.setText("Name");
			GridBagConstraints gbc_txtName = new GridBagConstraints();
			gbc_txtName.insets = new Insets(0, 0, 5, 5);
			gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtName.gridx = 1;
			gbc_txtName.gridy = 1;
			contentPanel.add(txtName, gbc_txtName);
			txtName.setColumns(10);
		}
		{
			JLabel lblPlayed = new JLabel("Played:");
			GridBagConstraints gbc_lblPlayed = new GridBagConstraints();
			gbc_lblPlayed.anchor = GridBagConstraints.EAST;
			gbc_lblPlayed.insets = new Insets(0, 0, 5, 5);
			gbc_lblPlayed.gridx = 2;
			gbc_lblPlayed.gridy = 1;
			contentPanel.add(lblPlayed, gbc_lblPlayed);
		}
		{
			txtPlayed = new JTextField();
			txtPlayed.setText("0");
			GridBagConstraints gbc_txtPlayed = new GridBagConstraints();
			gbc_txtPlayed.insets = new Insets(0, 0, 5, 0);
			gbc_txtPlayed.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtPlayed.gridx = 3;
			gbc_txtPlayed.gridy = 1;
			contentPanel.add(txtPlayed, gbc_txtPlayed);
			txtPlayed.setColumns(10);
		}
		{
			JLabel lblVote = new JLabel("Vote:");
			GridBagConstraints gbc_lblVote = new GridBagConstraints();
			gbc_lblVote.anchor = GridBagConstraints.EAST;
			gbc_lblVote.insets = new Insets(0, 0, 5, 5);
			gbc_lblVote.gridx = 0;
			gbc_lblVote.gridy = 2;
			contentPanel.add(lblVote, gbc_lblVote);
		}
		{
			txtVote = new JTextField();
			txtVote.setText("0");
			GridBagConstraints gbc_txtVote = new GridBagConstraints();
			gbc_txtVote.insets = new Insets(0, 0, 5, 5);
			gbc_txtVote.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtVote.gridx = 1;
			gbc_txtVote.gridy = 2;
			contentPanel.add(txtVote, gbc_txtVote);
			txtVote.setColumns(10);
		}
		{
			JLabel lblTotalVotes = new JLabel("Total Votes:");
			GridBagConstraints gbc_lblTotalVotes = new GridBagConstraints();
			gbc_lblTotalVotes.insets = new Insets(0, 0, 5, 5);
			gbc_lblTotalVotes.anchor = GridBagConstraints.EAST;
			gbc_lblTotalVotes.gridx = 2;
			gbc_lblTotalVotes.gridy = 2;
			contentPanel.add(lblTotalVotes, gbc_lblTotalVotes);
		}
		{
			txtTotalVotes = new JTextField();
			txtTotalVotes.setText("0");
			GridBagConstraints gbc_txtTotalVotes = new GridBagConstraints();
			gbc_txtTotalVotes.insets = new Insets(0, 0, 5, 0);
			gbc_txtTotalVotes.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTotalVotes.gridx = 3;
			gbc_txtTotalVotes.gridy = 2;
			contentPanel.add(txtTotalVotes, gbc_txtTotalVotes);
			txtTotalVotes.setColumns(10);
		}
		{
			chckbxConstructed = new JCheckBox("Constructed");
			GridBagConstraints gbc_chckbxConstructed = new GridBagConstraints();
			gbc_chckbxConstructed.anchor = GridBagConstraints.WEST;
			gbc_chckbxConstructed.insets = new Insets(0, 0, 5, 5);
			gbc_chckbxConstructed.gridx = 1;
			gbc_chckbxConstructed.gridy = 3;
			contentPanel.add(chckbxConstructed, gbc_chckbxConstructed);
		}
		{
			JLabel lblCards = new JLabel("Cards:");
			GridBagConstraints gbc_lblCards = new GridBagConstraints();
			gbc_lblCards.anchor = GridBagConstraints.EAST;
			gbc_lblCards.insets = new Insets(0, 0, 5, 5);
			gbc_lblCards.gridx = 0;
			gbc_lblCards.gridy = 4;
			contentPanel.add(lblCards, gbc_lblCards);
		}
		{
			txtCards = new JTextField();
			txtCards.setText("card1,card2,...,card10");
			GridBagConstraints gbc_txtCards = new GridBagConstraints();
			gbc_txtCards.insets = new Insets(0, 0, 5, 0);
			gbc_txtCards.gridwidth = 3;
			gbc_txtCards.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtCards.gridx = 1;
			gbc_txtCards.gridy = 4;
			contentPanel.add(txtCards, gbc_txtCards);
			txtCards.setColumns(10);
		}
		{
			JLabel lblSets = new JLabel("Sets:");
			GridBagConstraints gbc_lblSets = new GridBagConstraints();
			gbc_lblSets.anchor = GridBagConstraints.EAST;
			gbc_lblSets.insets = new Insets(0, 0, 0, 5);
			gbc_lblSets.gridx = 0;
			gbc_lblSets.gridy = 5;
			contentPanel.add(lblSets, gbc_lblSets);
		}
		{
			txtSets = new JTextField();
			txtSets.setText("set1,set2,...");
			GridBagConstraints gbc_txtSets = new GridBagConstraints();
			gbc_txtSets.gridwidth = 3;
			gbc_txtSets.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtSets.gridx = 1;
			gbc_txtSets.gridy = 5;
			contentPanel.add(txtSets, gbc_txtSets);
			txtSets.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						if (parent != null) {

							if (parent.deckIDList.contains(txtNewdeck.getText())) {

								JOptionPane.showMessageDialog((Component) arg0.getSource(),
										"ID alredy taken, please change ID!");
							} else {
								parent.addDeck(parent.new Deck(txtNewdeck.getText(), txtName.getText(),
										chckbxConstructed.isSelected(), Float.parseFloat(txtVote.getText()),
										Integer.parseInt(txtTotalVotes.getText()),
										Integer.parseInt(txtPlayed.getText()),
										new ArrayList<String>(Arrays.asList(txtCards.getText().split(","))),
										new ArrayList<String>(Arrays.asList(txtSets.getText().split(",")))));
								NewDeckDialog.this.dispose();
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						NewDeckDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
