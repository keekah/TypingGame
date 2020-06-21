package com.kika.typinggame;

import java.awt.*;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class GameScreen extends JFrame
{
	private ArrayList<String> words;
	
	private String displayWord;
	private JLabel displayWordLabel;
	
	private String wordTyped;
	private JLabel wordTypedLabel;
	
	
	private boolean typedCorrectly; 
	
	private Component listeningPane;
	
	private JLabel scoreLabel;
	private JLabel wpmLabel;
	private JLabel accuracyLabel;
	private JLabel numericScoreLabel;
	private JLabel numericWPMLabel;
	private JLabel numericAccuracyLabel;
	
	private int score;
	private int numberOfWordsTyped;
	private int numberOfWordsPresented; 
	private int numberOfCharactersTyped;
	private int numberOfMistypedCharacters;			// number of mistyped characters
	
	public GameScreen(String wordBank)
	{
		if (wordBank == null)
			throw new NullPointerException("wordBank");
		
		System.out.println("Starting new game with " + wordBank);
		
		words = new ArrayList<String>();
		wordTyped = "";
		typedCorrectly = false;
		
		score = 0;
		numberOfWordsPresented = 0;
		numberOfWordsTyped = 0;
		numberOfCharactersTyped = 0;
		numberOfMistypedCharacters = 0;
		
		makeFrame();
		makeStatusBar();
		makeButtons();
		makeCaptureArea();
		
		createWordBank("word-banks/GoT-names-places.txt");
		
		listeningPane = getGlassPane();
		listeningPane.setFocusTraversalKeysEnabled(false);
		listeningPane.setFocusable(true);
		listeningPane.setVisible(true);
		listeningPane.addKeyListener(new KeyListener()
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
					incrementCharactersTyped();
					
				}
				
				
				if (wordTyped.equals(displayWord))
				{
					typedCorrectly = true;
					JOptionPane.showMessageDialog(GameScreen.this,  "Congratulations!");
					clearInput();
					displayWord = getRandomWord();
					displayWordLabel.setText(displayWord);
					System.out.println(numberOfWordsPresented);
					numericAccuracyLabel.setText(String.valueOf(getAccuracy()));
					incrementScore(displayWord.length());
				}
					
					
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && wordTyped.length() > 0)
				{
					wordTyped = wordTyped.substring(0, wordTyped.length()-1);
					wordTypedLabel.setText(wordTyped);
					incrementErrors();
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
		String nextWord = words.get(index);
		
		// count the number of individual words in nextWord (which may be more than one, e.g. nextWord = "FirstName LastName");
		String [] individualWords = nextWord.split(" ", -1);
		numberOfWordsPresented += individualWords.length;
		
//		for (String s : individualWords)
//		{
//			System.out.print(s + " ");
//		}
//		
//		System.out.println();
		
		return nextWord;
	}
	
	// TODO clean this up, maybe change to startGame()
	private void play(int n)
	{
		do
		{
			displayWord = getRandomWord();
			displayWordLabel.setText(displayWord);
			
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
			{
				String word = in.nextLine().trim();
				if (!words.add(word))
					System.out.println(word);
				
//				words.add(in.nextLine().trim());
				
			}
		}
		// TODO exceptions more thoroughly 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void incrementScore(int points)
	{
		score += points;
		numericScoreLabel.setText(String.valueOf(score));
	}
	
	private void setDisplayLabel(String displayWord)
	{
		displayWordLabel.setText(displayWord);
	}
	
	private void makeCaptureArea()
	{
		JPanel centerArea = new JPanel(new GridLayout(2,1));
		
			JPanel displayArea = new JPanel(new GridBagLayout());
			displayWordLabel = new JLabel();
			displayArea.add(displayWordLabel);
			
			JPanel captureArea = new JPanel(new GridBagLayout());
			wordTypedLabel = new JLabel();
			captureArea.add(wordTypedLabel);
			
//			captureArea.setBackground(new Color(125, 0, 125, 100));
			
		centerArea.add(displayArea);
		centerArea.add(captureArea);
		
		add(centerArea, BorderLayout.CENTER);
		
	}
	
	public void makeFrame()
	{
//		setSize(800,1000);
		setPreferredSize(new Dimension(800,1000));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		pack();
		setVisible(true);
	}
	
	private void makeStatusBar()
	{
		scoreLabel = new JLabel("Score: ");
		wpmLabel = new JLabel("WPM: ");
		accuracyLabel = new JLabel("Accuracy: ");
		
		numericScoreLabel = new JLabel();
		numericWPMLabel = new JLabel();
		numericAccuracyLabel = new JLabel();
		
		JPanel statusPanel = new JPanel();
		statusPanel.add(scoreLabel);
		statusPanel.add(numericScoreLabel);
		statusPanel.add(wpmLabel);
		statusPanel.add(numericWPMLabel);
		statusPanel.add(accuracyLabel);
		statusPanel.add(numericAccuracyLabel);
		
		add(statusPanel, BorderLayout.NORTH);
	}
	
	private void makeButtons()
	{
		JButton quitButton = new JButton("Quit");
		JButton pauseButton = new JButton("Pause");
		JButton restartButton = new JButton("Restart");
		
		quitButton.addActionListener(new ActionListener()
															{
																public void actionPerformed(ActionEvent e)
																{
																	System.exit(0);
																}
															});
		
		restartButton.addActionListener(new ActionListener()
															{
																public void actionPerformed(ActionEvent e)
																{
//																	listeningPane.setFocusable(false);
																	clearInput();
																	listeningPane.requestFocus();
																}
															});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(quitButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(restartButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void clearInput()
	{
		wordTyped = "";
		wordTypedLabel.setText(wordTyped);
	}
	
	private void incrementErrors()
	{
		numberOfMistypedCharacters++;
	}
	
	private void incrementCharactersTyped()
	{
		numberOfCharactersTyped++;
	}
	
	private double getAccuracy()
	{
		double accuracy = numberOfCharactersTyped - numberOfMistypedCharacters;
		accuracy /= numberOfCharactersTyped;
		
		
		return accuracy;
	}
	
	public static void main(String [] args)
	{
		new GameScreen("Game of Thrones");
	}
}

