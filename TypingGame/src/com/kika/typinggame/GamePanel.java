package com.kika.typinggame;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import java.io.*;
import java.util.*;

public class GamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private Component listeningPane;
	
	private boolean allowBackspacing;
	
	private JLabel numericScoreLabel;
	private JLabel numericWPMLabel;
	private JLabel numericAccuracyLabel;
	
	private DisplayWord dw;
	private String userInput;
	
	private int score;
	private int numberOfWordsTyped;
	private int numberOfWordsPresented; 
	private int numCharactersTyped;
	private int numErrors;		// number of mistyped characters	
	
	private ArrayList<String> words;
	
	public GamePanel(String wordBank, GameFrame frame)
	{
		if (wordBank == null)
			throw new NullPointerException("wordBank");
		
		allowBackspacing = true;
		userInput = "";
		numCharactersTyped = 0;
		numErrors = 0;
		
		initializeStatusAndButtonBars();
		initializeListener(frame);
		
		loadWordBank(wordBank);
		getDisplayWord();

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
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
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
		numericAccuracyLabel = new JLabel("0");
		accuracyPanel.add(accuracyLabel);
		accuracyPanel.add(numericAccuracyLabel);
		
		JPanel statusBar = new JPanel(new GridLayout(1,3));
		statusBar.add(scorePanel);
		statusBar.add(wpmPanel);
		statusBar.add(accuracyPanel);

		
		add(statusBar, BorderLayout.NORTH);
		
		
		// Create the lower bar containing buttons and options
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
					getDisplayWord();
					userInput = "";
					repaint();
					listeningPane.requestFocus();
				}
			});
		

		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(quitButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(restartButton);
		
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	private void getDisplayWord()
	{
		String word = getRandomWord();
		dw = new DisplayWord(word, 0, 0);
	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		Font f = new Font("Serif", Font.BOLD, 36);
		g2.setFont(f);
		
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(dw.getWord(), context);
		
		// center the string according to the bounds of its enclosing rectangle
		double x = (getWidth() - bounds.getWidth()) / 2;
		double y = (getHeight() - bounds.getHeight()) / 2;
		
		double ascent = -bounds.getY();
		y += ascent;
		
		dw.setX((int)x);
		dw.setY((int)y);
		
		drawWord(g2);
		
		if (userInput.length() != 0)
			drawPartiallyColoredWord(g2);
	}
	
	// Draw the word in black
	private void drawWord(Graphics2D g2)
	{
		g2.drawString(dw.getWord(), dw.getX(), dw.getY());
	}
	
	// This will be called if the user's input thus far matches the word displayed.
	// Draw the user's input in blue on top of the displayed word.
	// Length of userInput is guaranteed to be less than or equal to the length 
	// of the display word when this method is called.
	private void drawPartiallyColoredWord(Graphics2D g2)
	{
		String matchingSubstring = dw.getWord().substring(0, userInput.length());
		
		// fun colors
		Color lightBlue = new Color(52, 198, 235);
		Color purple = new Color(131, 52, 235);
		Color darkMagenta = new Color(176, 28, 139);
		
		
		g2.setColor(darkMagenta);
		g2.drawString(matchingSubstring, dw.getX(), dw.getY());
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
					// TODO change this isLetter check to include symbols/special characters
					if (isValidKey(c))
					{
						userInput += c;
						System.out.println("UserInput: " + userInput);
						System.out.println();
						incrementCharactersTyped();
						
						if (!inputMatches())
							userInput = "";
						
						repaint();
					}
					
					
					if (userInput.equals(dw.getWord()))
					{
						numericAccuracyLabel.setText(getAccuracyString());
						incrementScore(dw.getWord().length());
						userInput = "";
						getDisplayWord();
					}
				}
	
				@Override
				public void keyPressed(KeyEvent e)
				{
					if (allowBackspacing)
					{
						if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && userInput.length() > 0)
						{
							userInput = userInput.substring(0, userInput.length()-1);
							repaint();
							incrementErrors();
						}
					}
				}
	
				@Override
				public void keyReleased(KeyEvent e){}
			});
	}
	
	// Compare the user input with the display word
	private boolean inputMatches()
	{
		if (userInput.length() > dw.getWord().length())
			return false;
		
		String s = dw.getWord().substring(0, userInput.length());
		
		return userInput.equals(s);
	}
	
	// check to ensure that the key typed is a letter, number, symbol, or space before appending it to wordTyped
	// i.e. exclude backspace characters, enter, shift, etc 
	// TODO may need to move this to the keypressed method, maybe call on VK constants instead of Character methods
	private boolean isValidKey(char c)
	{
		return Character.isDigit(c) || Character.isLetter(c) || Character.isSpaceChar(c) || c == '\'' || c == ',';
	}
	
	private String getAccuracyString()
	{
		double accuracy = numCharactersTyped - numErrors;
		accuracy /= numCharactersTyped;
		accuracy *= 100;
		
		return String.format("%.2f", accuracy);
	}
	
	private void incrementScore(int points)
	{
		score += points;
		numericScoreLabel.setText(String.valueOf(score));
	}
	
	private void incrementErrors()
	{
		numErrors++;
	}
	
	private void incrementCharactersTyped()
	{
		numCharactersTyped++;
	}
	
	// retrieve a word at random from the hashset
	private String getRandomWord()
	{
		int index = (int)(Math.random() * words.size());
		String nextWord = words.get(index);
		
		// count the number of individual words in nextWord (which may be more than one, e.g. nextWord = "FirstName LastName");
		String [] individualWords = nextWord.split(" ", -1);
		numberOfWordsPresented += individualWords.length;
		
		return nextWord;
	}
}

