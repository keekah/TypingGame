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
	
	private Image backgroundImage;
	
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
	
	private StringGenerator stringGenerator;
	
	private ArrayList<String> words;
	
	public GamePanel(String wordBank, GameFrame frame)
	{
		if (wordBank == null)
			throw new NullPointerException("wordBank");
		
		setLayout(new BorderLayout());
		
		allowBackspacing = true;
		userInput = "";
		numCharactersTyped = 0;
		numErrors = 0;
		
		initializeStatusAndButtonBars();
		initializeListener(frame);
		
		// GOT methods
//		loadWordBank(wordBank);
//		getDisplayWord();
		
		// RandomStrings methods
		stringGenerator = new StringGenerator();
		getDisplayWord();
		
		
		backgroundImage = Toolkit.getDefaultToolkit().createImage("backgrounds/matrix.jpg");
		
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(backgroundImage, 0);
		
		try
		{
			tracker.waitForAll();
		}
		catch (InterruptedException e)
		{
			System.out.println("POOPIE");
			e.printStackTrace();
		}
		
		repaint();

	}
	
	private void getDisplayWord()
	{
		dw = new DisplayWord(stringGenerator.getHexLiteral());
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
		// make the status bar
		JPanel scorePanel = new JPanel();
		JLabel scoreLabel = new JLabel("Score: ");
		numericScoreLabel = new JLabel("0");
		scoreLabel.setForeground(Color.white);
		numericScoreLabel.setForeground(Color.white);
		scorePanel.add(scoreLabel);
		scorePanel.add(numericScoreLabel);
		
		JPanel wpmPanel = new JPanel();
		JLabel wpmLabel = new JLabel("WPM: ");
		numericWPMLabel = new JLabel("0");
		wpmLabel.setForeground(Color.white);
		numericWPMLabel.setForeground(Color.white);
		wpmPanel.add(wpmLabel);
		wpmPanel.add(numericWPMLabel);
		
		JPanel accuracyPanel = new JPanel();
		JLabel accuracyLabel = new JLabel("Accuracy: ");
		numericAccuracyLabel = new JLabel("0");
		accuracyLabel.setForeground(Color.white);
		numericAccuracyLabel.setForeground(Color.white);
		accuracyPanel.add(accuracyLabel);
		accuracyPanel.add(numericAccuracyLabel);
		
		JPanel statusBar = new JPanel(new GridLayout(1,3));
		int transparency = (int)(0.2*255);
		Color whiteTransparent = new Color(255, 255, 255, transparency);
		Color transparent = new Color(255, 255, 255, 0);
		
		statusBar.setBackground(whiteTransparent);
		scorePanel.setBackground(whiteTransparent);
		wpmPanel.setBackground(whiteTransparent);
		accuracyPanel.setBackground(whiteTransparent);
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
		buttonPanel.setBackground(whiteTransparent);
		buttonPanel.add(quitButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(restartButton);
		
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	// GoT
//	private void getDisplayWord()
//	{
//		dw = new DisplayWord(getRandomWord());
//	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		/*********/
//		 * trying to set image background
	
		
		//GoT
		// overlay a white semi-transparent rectangle on the background image to make it brighter
//		Rectangle2D whiteScreen = new Rectangle(getWidth(), getHeight());
//		int transparency = (int)(0.2*255);
//		Color whiteTransparent = new Color(255, 255, 255, transparency);
		
		g2.drawImage(backgroundImage, 0, 0, null);
		
		// GoT
//		g2.setColor(whiteTransparent);
//		g2.fill(whiteScreen);
//		g2.draw(whiteScreen);

		// Got:
//		g2.setColor(Color.black);
		
		g2.setColor(Color.white);
		
		/**********************************/
		
		Font f = new Font("Dialog", Font.BOLD, 36);
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
	
		if (userInput.length() > 0)
			drawPartiallyColoredWord(g2);
		
		
//		int dy = 10;
		
//		for (int i = 0; i < 300; i++)
//		{
//			 
//			g2.drawImage(backgroundImage, 0, 0, null);
//			dw.setY(dw.getY()+dy);
//			
//			drawWord(g2);
//		
//			if (userInput.length() > 0)
//				drawPartiallyColoredWord(g2);
//			
//			repaint();
//		}

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
	
	// retrieve a word at random from the list of words
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

