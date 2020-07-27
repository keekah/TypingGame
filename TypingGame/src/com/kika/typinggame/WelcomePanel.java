package com.kika.typinggame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class WelcomePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private String wordBank;
	
	public WelcomePanel(GameFrame frame)
	{
		// Divide WelcomePanel into a top panel and a bottom panel
		setLayout(new GridLayout(2,1));
		
		// Initialize the top panel and center its elements by using a GridBag with no constraints
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridBagLayout());
		
		JLabel label = new JLabel("Select a word bank: ");
		
		// Populate word bank drop down 
		String [] wordBanks = {"Game of Thrones", "Literary Quotes", "Symbols", "Letters"};
		JComboBox<String> dropDown = new JComboBox<String>(wordBanks);
		
		// Set a default selection
		wordBank = "Game of Thrones";
		
		dropDown.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					wordBank = (String)dropDown.getSelectedItem();
				}
			});
		
		
		selectionPanel.add(label);
		selectionPanel.add(dropDown);
		
		add(selectionPanel);
		
		
		// Initialize bottom panel
		JPanel buttonPanel  = new JPanel();
		
		JButton quitButton = new JButton("Quit");
		JButton viewHiScoresButton = new JButton("Hi Scores");
		JButton playButton = new JButton("Play");
		
		quitButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});
		
		//TODO view hi scores 
		viewHiScoresButton.setEnabled(false);
		
		playButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					frame.setContentPane(new GamePanel(wordBank, frame));
					frame.repaint();
				}
			});
		
		buttonPanel.add(quitButton);
		buttonPanel.add(viewHiScoresButton);
		buttonPanel.add(playButton);

		add(buttonPanel);
		
		
		// allow user to select play using the enter key
		frame.getRootPane().setDefaultButton(playButton);
	}		
}