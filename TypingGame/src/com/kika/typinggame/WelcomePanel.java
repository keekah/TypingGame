package com.kika.typinggame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;


public class WelcomePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private String wordBank;
	private List<GamePanelSettings> settingsList;
	
	public WelcomePanel(GameFrame frame)
	{
		// Load the settings data for each available word bank
		PropertiesBasedGamePanelSettingsLoader loader = new PropertiesBasedGamePanelSettingsLoader("game-settings");
		settingsList = loader.load();
		
		setLayout(new GridLayout(2,1));
		initializeTopPanel();
		initializeBottomPanel(frame);
	}
	
	private void initializeTopPanel()
	{
		JPanel selectionPanel = new JPanel();
		// Center its elements by using a GridBag with no constraints	
		selectionPanel.setLayout(new GridBagLayout());
		
		JLabel label = new JLabel("Select a word bank: ");
		
		JComboBox<String> dropDown = populateWordBankDropdown();

		// Set a default selection to avoid null pointer
		wordBank = dropDown.getItemAt(0);
		
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
	}
	
	private JComboBox<String> populateWordBankDropdown()
	{
		List<String> wordBanks = new ArrayList<String>();
		
		for (GamePanelSettings gps : settingsList)
			wordBanks.add(gps.getName());
		
		return new JComboBox<String>(wordBanks.toArray(new String[wordBanks.size()]));
	}
	
	private void initializeBottomPanel(GameFrame frame)
	{
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
		
		//TODO view high scores 
		viewHiScoresButton.setEnabled(false);
		
		playButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					frame.setContentPane(new GamePanel(wordBank, frame));
					frame.repaint();
				}
			});
		
		// allow user to select play using the enter key
		frame.getRootPane().setDefaultButton(playButton);
		
		buttonPanel.add(quitButton);
		buttonPanel.add(viewHiScoresButton);
		buttonPanel.add(playButton);

		add(buttonPanel);
	}
}






















