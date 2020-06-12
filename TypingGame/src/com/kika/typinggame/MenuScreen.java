package com.kika.typinggame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MenuScreen extends JFrame
{
	private JComboBox<String> wordBankDropdown;
	private String wordBank;
	private JButton playButton;
	private JButton quitButton;
	private JButton viewHiScoresButton;
	
	public MenuScreen()
	{
		makeFrame();
	}
	
	private void makeFrame()
	{
		setSize(800,1000);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(2, 1));
		
		makeDropDown();
		makeButtons();
		
		setVisible(true);
	}
	
	private void makeDropDown()
	{
		String [] wordBanks = {"Select a word bank: ", "Game of Thrones", "Literary Quotes", "Symbols", "Letters"};
		JLabel selectedWordBank = new JLabel();
		
		wordBankDropdown = new JComboBox(wordBanks);
		wordBankDropdown.addActionListener(new ActionListener(){
																	public void actionPerformed(ActionEvent e)
																	{
																		// TODO make sure a wordbank is selected before hitting play
																		wordBank = (String)wordBankDropdown.getSelectedItem();
																		System.out.println(wordBank);
																		selectedWordBank.setText(wordBank);
																	}
															   });
		
		JPanel wordBankPanel = new JPanel();
		wordBankPanel.add(wordBankDropdown);
		wordBankPanel.add(selectedWordBank);
		
		add(wordBankPanel);
	}
	
	private void makeButtons()
	{
		JPanel buttonPanel = new JPanel();
		
		quitButton = new JButton("Quit");
		viewHiScoresButton = new JButton("Hi Scores");
		playButton = new JButton("Play");
		
		quitButton.addActionListener(new ActionListener(){
															public void actionPerformed(ActionEvent e)
															{
																System.exit(0);
															}
														  });
		
		//TODO view hi scores 
		
		playButton.addActionListener(new ActionListener(){
															public void actionPerformed(ActionEvent e)
															{
																new GameScreen(wordBank);
															}
														 });
		
		buttonPanel.add(quitButton);
		buttonPanel.add(viewHiScoresButton);
		buttonPanel.add(playButton);
		
		add(buttonPanel);
	}
	
	public static void main(String [] args)
	{
		new MenuScreen();
	}
}
