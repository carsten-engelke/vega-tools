package org.vega.tools;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.vega.tools.VEGABackupExternalHD.BusyRobot;

public class DeleteFilesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private DefaultListModel<File> model = new DefaultListModel<File>();

	/**
	 * Create the dialog.
	 */
	public DeleteFilesDialog(ArrayList<File> deleteFileList, final VEGABackupExternalHD parent) {

		for (File f : deleteFileList) {
			model.addElement(f);
			;
		}

		setTitle("Delete selected files?");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		final JList<File> list = new JList<File>();
		JButton btnRemoveSelectedFile = new JButton(
				"Remove selected file from list");
		{

			btnRemoveSelectedFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					model.remove(list.getSelectedIndex());
				}
			});
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			contentPanel.add(scrollPane, gbc_scrollPane);

			scrollPane.setViewportView(list);
			list.setModel(model);
		}
		GridBagConstraints gbc_btnRemoveSelectedFile = new GridBagConstraints();
		gbc_btnRemoveSelectedFile.gridx = 1;
		gbc_btnRemoveSelectedFile.gridy = 0;
		contentPanel.add(btnRemoveSelectedFile, gbc_btnRemoveSelectedFile);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						dispose();
						BusyRobot busy = parent.new BusyRobot();
						busy.start();
						ArrayList<File> errorList = new ArrayList<File>();
						Enumeration<File> deleteList = model.elements();
						int delnum = 0;
						int prg = 0;
						parent.progressBar.setMaximum(model.getSize());
						while (deleteList.hasMoreElements()) {
							
							File del = deleteList.nextElement();
							prg++;
							if (prg % 100 == 0) {
								parent.progressBar.setValue(prg);
								parent.lblNewLabel.setText("DEL: " + del.getPath());
							}
							if (del.delete()) {
								
								delnum++;
							} else {
								
								errorList.add(del);
							}
						}
						if (errorList.size() > 0) {
							try {
								FileWriter fw = new FileWriter("error.txt");
								for (File e1 : errorList) {
									fw.write(e1.getPath() + "\n");
								}
								fw.close();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							parent.lblNewLabel.setText("DONE: " + delnum
									+ " files deleted, Errors: " + errorList.size()
									+ " - see errors.txt");
						} else {
							parent.lblNewLabel.setText("DONE: " + delnum + " files deleted");
						}
						
						parent.enable();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						parent.enable();
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
