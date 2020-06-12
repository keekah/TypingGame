package com.kika.typinggame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class GameScreen extends JFrame
{
	private ArrayList<String> words;
	
	private JLabel scoreLabel;
	private JLabel wpmLabel;
	private JLabel accuracyLabel;
	private JButton quitButton;
	private JButton pauseButton;
	private JButton restartButton;
	private JButton clearButton;
	
	private String wordTyped;
	private String wordToMatch;
	private JLabel wordTypedLabel;
	private JLabel wordToMatchLabel;
	private boolean typedCorrectly; 
	private JPanel captureArea;
	
	public GameScreen(String wordBank)
	{
		if (wordBank == null)
			throw new NullPointerException("wordBank");
		
		System.out.println("Starting new game with " + wordBank);
		
		words = new ArrayList<String>();
		wordTyped = "";
		wordTypedLabel = new JLabel();
		wordToMatchLabel = new JLabel();
		typedCorrectly = false;
		
		makeFrame();
		makeStatusBar();
		makeButtons();
		makeCaptureArea();
		
		createWordBank("word-banks/GoT-names-places.txt");
		
		System.out.println(words.size());
		
		// TODO add a glass pane and attach the key listener to that 
		
		addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				char c = e.getKeyChar();
				// TODO change these string concats to stringbuilder
				// TODO change this isLetter check to include symbols/special characters
				if (isValidKey(c))
				{
					wordTyped += c;
					wordTypedLabel.setText(wordTyped);
				}
				
				
				if (wordTyped.equals(wordToMatch))
				{
					typedCorrectly = true;
					JOptionPane.showMessageDialog(GameScreen.this,  "Congratulations!");
					clearInput();
					wordToMatch = getRandomWord();
					wordToMatchLabel.setText(wordToMatch);
				}
					
					
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && wordTyped.length() > 0)
				{
					wordTyped = wordTyped.substring(0, wordTyped.length()-1);
					wordTypedLabel.setText(wordTyped);
				}
					
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				
			}
		});
		
		play(10);
	}
	
	// check to ensure that the key typed is a letter, number, symbol, or space before appending it to wordTyped
	// i.e. exclude backspace characters, enter, shift, etc 
	// TODO may need to move this to the keypressed method, maybe call on VK constants instead of Character methods
	private boolean isValidKey(char c)
	{
		return Character.isDigit(c) || Character.isLetter(c) || Character.isSpaceChar(c) || c == '\'' || c == ',';
	}
	
	// retrieve a word at random from the hashset
	private String getRandomWord()
	{
		int index = (int)(Math.random() * words.size());
		return words.get(index);
	}
	
	// TODO clean this up, maybe change to startGame()
	private void play(int n)
	{
		do
		{
			wordToMatch = getRandomWord();
			wordToMatchLabel.setText(wordToMatch);
			
			while (true)
			{
				if (typedCorrectly)
				{
					n--;
					break;
				}
			}
		} while(n > 0);
		
	}
	
	private void createWordBank(String filename)
	{
		try (Scanner in = new Scanner(new File(filename)))
		{
			while (in.hasNextLine())
				words.add(in.nextLine().trim());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void makeCaptureArea()
	{
		JPanel centerArea = new JPanel(new GridLayout(2,1));
		
		captureArea = new JPanel();
		captureArea.add(wordToMatchLabel);

		
		JPanel displayArea = new JPanel();
		displayArea.add(wordTypedLabel);
		
		centerArea.add(captureArea);
		centerArea.add(displayArea);
		
		add(centerArea, BorderLayout.CENTER);
		
		
	}
	
	public void makeFrame()
	{
		setSize(800,1000);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		setVisible(true);
	}
	
	private void makeStatusBar()
	{
		scoreLabel = new JLabel("Score: ");
		wpmLabel = new JLabel("WPM: ");
		accuracyLabel = new JLabel("Accuracy: ");
		
		JPanel statusPanel = new JPanel();
		statusPanel.add(scoreLabel);
		statusPanel.add(wpmLabel);
		statusPanel.add(accuracyLabel);
		
		add(statusPanel, BorderLayout.NORTH);
	}
	
	private void makeButtons()
	{
		quitButton = new JButton("Quit");
		pauseButton = new JButton("Pause");
		restartButton = new JButton("Restart");
		clearButton = new JButton("Clear");
		
		quitButton.addActionListener(new ActionListener()
															{
																public void actionPerformed(ActionEvent e)
																{
																	System.exit(0);
																}
															});
		
		clearButton.addActionListener(new ActionListener()
															{
																public void actionPerformed(ActionEvent e)
																{
																	clearInput();
																}
															});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(quitButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(restartButton);
		buttonPanel.add(clearButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void clearInput()
	{
		wordTyped = "";
		wordTypedLabel.setText(wordTyped);
	}
	
	public static void main(String [] args)
	{
		new GameScreen("Game of Thrones");
	}
}