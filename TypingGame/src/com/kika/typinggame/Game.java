package com.kika.typinggame;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;

public class Game
{
	public static void main(String [] args)
	{
		EventQueue.invokeLater(new Runnable()
			{
				public void run()
				{
					GameFrame frame = new GameFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
				
			});
	}

}

class GameFrame extends JFrame
{	
	public GameFrame()
	{
		// set the frame size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		setSize(width/4, height/2);	
		setLocationRelativeTo(null);
//		setLocationByPlatform(true);

		WelcomePanel welcomeScreen = new WelcomePanel(this);
		setContentPane(welcomeScreen);
	}
}

class WelcomePanel extends JPanel
{
	private String wordBank;
	
	public WelcomePanel(GameFrame frame)
	{
		setLayout(new GridLayout(2,1));
		
		// top panel
		JPanel selectionPanel = new JPanel();
		
		JLabel label = new JLabel("Select a word bank: ");
		String [] wordBanks = {"Game of Thrones", "Literary Quotes", "Symbols", "Letters"};
		JComboBox dropDown = new JComboBox(wordBanks);
		wordBank = "Game of Thrones";	// set default selection
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
		
		
		// bottom panel
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
		
	}
}

class GamePanel extends JPanel
{
	private Component listeningPane;
	
	private String displayWord;
	private String wordTyped;
	private boolean typedCorrectly; 
	
	private JLabel displayWordLabel;
	private JLabel typedWordLabel;
	
	private JLabel numericScoreLabel;
	private JLabel numericWPMLabel;
	private JLabel numericAccuracyLabel;
	
	private int score;
	private int numberOfWordsTyped;
	private int numberOfWordsPresented; 
	private int numberOfCharactersTyped;
	private int numberOfMistypedCharacters;	
	
	private HashMap<String, String> files;	// the files associated with names of wordbanks
	
	private ArrayList<String> words;
	
	public GamePanel(String wordBank, GameFrame frame)
	{
		if (wordBank == null)
			throw new NullPointerException("wordBank");
		
		initializeFiles();
		initializeStatusAndButtonBars();
		initializeCenterArea();
		initializeListener(frame);
		
		loadWordBank(wordBank);
		
		clearInput();
		displayNewWord();
	}

	private void loadWordBank(String wordBank)
	{
		words = new ArrayList<String>();
		
		String filename = "word-banks/GoT-names-places.txt";
		
		try (Scanner in = new Scanner(new File(filename)))
		{
			while (in.hasNextLine())
			{
				String word = in.nextLine().trim();
				
				if (word.length() > 0)
					if (!words.add(word))
						System.out.println(word);
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
	
	private void initializeFiles()
	{
		files = new HashMap<String, String>();
		
		// TODO add more files
		files.put("Game of Thrones", "GoT-names-places.txt");
	}
	
	private void displayNewWord()
	{
		displayWord = getRandomWord();
		displayWordLabel.setText(displayWord);
	}
	
	private void initializeStatusAndButtonBars()
	{
		setLayout(new BorderLayout());
		
		// make the status bar
		JPanel scorePanel = new JPanel();
		JLabel scoreLabel = new JLabel("Score: ");
		numericScoreLabel = new JLabel("0");
		scorePanel.add(scoreLabel);
		scorePanel.add(numericScoreLabel);
		
		JPanel wpmPanel = new JPanel();
		JLabel wpmLabel = new JLabel("WPM: ");
		numericWPMLabel = new JLabel("0");
		wpmPanel.add(wpmLabel);
		wpmPanel.add(numericWPMLabel);
		
		JPanel accuracyPanel = new JPanel();
		JLabel accuracyLabel = new JLabel("Accuracy: ");
		numericAccuracyLabel = new JLabel();
		accuracyPanel.add(accuracyLabel);
		accuracyPanel.add(numericAccuracyLabel);
		
		JPanel statusBar = new JPanel(new GridLayout(1,3));
		statusBar.add(scorePanel);
		statusBar.add(wpmPanel);
		statusBar.add(accuracyPanel);
		
		add(statusBar, BorderLayout.NORTH);
		
		
		// make the button area
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
		
		// TODO pause button / timer
		pauseButton.setEnabled(false);
		
		restartButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					//	listeningPane.setFocusable(false);
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
	
	private void initializeCenterArea()
	{
		JPanel displayArea = new JPanel(new GridBagLayout());
		displayWordLabel = new JLabel();
		displayArea.add(displayWordLabel);
		
		JPanel captureArea = new JPanel(new GridBagLayout());
		typedWordLabel = new JLabel();
		captureArea.add(typedWordLabel);
		
		JPanel centerArea = new JPanel(new GridLayout(2,1));
		centerArea.add(displayArea);
		centerArea.add(captureArea);
		
		add(centerArea, BorderLayout.CENTER);
	}
	
	private void initializeListener(GameFrame frame)
	{
		listeningPane = frame.getGlassPane();
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
					typedWordLabel.setText(wordTyped);
					incrementCharactersTyped();
					
				}
				
				
				if (wordTyped.equals(displayWord))
				{
					typedCorrectly = true;
					clearInput();
					displayNewWord();
//					System.out.println(numberOfWordsPresented);
					numericAccuracyLabel.setText(getAccuracyString());
					incrementScore(displayWord.length());
				}
					
					
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && wordTyped.length() > 0)
				{
					wordTyped = wordTyped.substring(0, wordTyped.length()-1);
					typedWordLabel.setText(wordTyped);
					incrementErrors();
				}
					
			}

			@Override
			public void keyReleased(KeyEvent e){}
			
		});
	}
	
	// check to ensure that the key typed is a letter, number, symbol, or space before appending it to wordTyped
	// i.e. exclude backspace characters, enter, shift, etc 
	// TODO may need to move this to the keypressed method, maybe call on VK constants instead of Character methods
	private boolean isValidKey(char c)
	{
		return Character.isDigit(c) || Character.isLetter(c) || Character.isSpaceChar(c) || c == '\'' || c == ',';
	}
	
	private void clearInput()
	{
		wordTyped = "";
		typedWordLabel.setText(wordTyped);
	}
	
	private String getAccuracyString()
	{
		double accuracy = numberOfCharactersTyped - numberOfMistypedCharacters;
		accuracy /= numberOfCharactersTyped;
		accuracy *= 100;
		
		
		return String.valueOf(accuracy);
	}
	
	private void incrementScore(int points)
	{
		score += points;
		numericScoreLabel.setText(String.valueOf(score));
	}
	
	private void incrementErrors()
	{
		numberOfMistypedCharacters++;
	}
	
	private void incrementCharactersTyped()
	{
		numberOfCharactersTyped++;
	}
	
	// retrieve a word at random from the hashset
	private String getRandomWord()
	{
		int index = (int)(Math.random() * words.size());
		String nextWord = words.get(index);
		
		// count the number of individual words in nextWord (which may be more than one, e.g. nextWord = "FirstName LastName");
		String [] individualWords = nextWord.split(" ", -1);
		numberOfWordsPresented += individualWords.length;
		
//			for (String s : individualWords)
//			{
//				System.out.print(s + " ");
//			}
//			
//			System.out.println();
		
		return nextWord;
	}
	
	
}
