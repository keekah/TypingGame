package com.kika.typinggame;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;


public class GamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Component listeningPane;
	private Image backgroundImage;
	private boolean allowBackspacing;
	private JLabel numericScoreLabel;
	private JLabel numericWPMLabel;
	private JLabel numericAccuracyLabel;
	private DisplayWord displayWord;
	private String userInput;
	private int score;
	private int numCharactersTyped;
	private int numErrors;		// mistyped characters	
	private GamePanelSettings settings;
	private GameFrame frame;
	private Set<Character> validChars;
	
	public GamePanel(GamePanelSettings settings, GameFrame frame)
	{
		this.settings = settings;
		this.frame = frame;
		
		validChars = defineValidCharacters();
		
		setLayout(new BorderLayout());
		initializeStatusBar();
		initializeButtonBar();
		initializeKeyListener();
		setBackgroundImage();
		
		allowBackspacing = true;
		userInput = "";
		numCharactersTyped = 0;
		numErrors = 0;

		displayNewWord();
	}
	
	private Set<Character> defineValidCharacters()
	{
		Set<Character> set = new HashSet<Character>();
		
		String nonAlphaNumerics = "!@#$%^&*-_=+\\/{}()[]<>?,.\"\':; |`~";
		String numerals = "0123456789";
		String alphas = "abcdefghijklmnopqrstuvwxyz";
		
		for (int i = 0; i < nonAlphaNumerics.length(); i++)
			set.add(nonAlphaNumerics.charAt(i));
		
		for (int i = 0; i < numerals.length(); i++)
			set.add(numerals.charAt(i));
		
		for (int i = 0; i < alphas.length(); i++)
		{
			char c = alphas.charAt(i);
			set.add(c);
			set.add(Character.toUpperCase(c));
		}

		return set;
	}
	
	private void initializeStatusBar()
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
		scorePanel.setBackground(transparent);
		wpmPanel.setBackground(transparent);
		accuracyPanel.setBackground(transparent);
		
		
		statusBar.add(scorePanel);
		statusBar.add(wpmPanel);
		statusBar.add(accuracyPanel);

		add(statusBar, BorderLayout.NORTH);
	}
	
	private void initializeButtonBar()
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
		
		// TODO pause button / timer
		pauseButton.setEnabled(false);
		

		
		restartButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					displayNewWord();
					userInput = "";
					repaint();
					listeningPane.requestFocus();
				}
			});
		

		
		JPanel buttonPanel = new JPanel();
		int transparency = (int)(0.2*255);
		Color whiteTransparent = new Color(255, 255, 255, transparency);
		buttonPanel.setBackground(whiteTransparent);
		
		buttonPanel.add(quitButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(restartButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void initializeKeyListener()
	{
		listeningPane = frame.getGlassPane();
		listeningPane.setFocusTraversalKeysEnabled(false);
		listeningPane.setFocusable(true);
		listeningPane.setVisible(true);
		listeningPane.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					processKeyTyped(e.getKeyChar());
				}
			});
	}
	
	private void setBackgroundImage()
	{
		backgroundImage = settings.getBackgroundImage();
		
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(backgroundImage, 0);
		
		try
		{
			tracker.waitForAll();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void displayNewWord()
	{
		displayWord = new DisplayWord(getRandomWord(settings.getWordBank()));
	}
	
	// Retrieve a word at random from the word bank.
	private String getRandomWord(List<String> wordBank)
	{
		int index = (int)(Math.random() * wordBank.size());
		
		return wordBank.get(index);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		drawBackgroundImage(g2);
		
		drawWord(g2);
	
		if (userInput.length() > 0)
			drawPartiallyColoredWord(g2);
	}

	private void drawBackgroundImage(Graphics2D g2)
	{
		double imageWidth = backgroundImage.getWidth(null);
		double imageHeight = backgroundImage.getHeight(null);
		
		double panelWidth = getWidth();
		double panelHeight = getHeight();
		
		double widthRatio =  panelWidth / imageWidth;
		double heightRatio = panelHeight / imageHeight;
		
		// Choose the largest scale factor to ensure that the image always
		// fills the panel. Image dimensions will always be greater than or
		// equal to the panel dimensions.
		double scaleFactor = Math.max(widthRatio, heightRatio);
		
		double scaledImageWidth = scaleFactor * imageWidth;
		double scaledImageHeight = scaleFactor * imageHeight; 
		
		// Center the image in the panel
		double X = (panelWidth - scaledImageWidth) / 2;
		double Y = (panelHeight - scaledImageHeight) / 2;
		
		AffineTransform previousTransform = g2.getTransform();
		
		AffineTransform transform = new AffineTransform();
		transform.translate(X, Y);
		transform.scale(scaleFactor, scaleFactor);
		
		g2.setTransform(transform);
		
		g2.drawImage(backgroundImage, 0, 0, null);
		
		g2.setTransform(previousTransform);
	}
	
	// Draw the word in black
	private void drawWord(Graphics2D g2)
	{
		Font font = settings.getFont();
		g2.setFont(font);

		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(displayWord.getWord(), context);
		
		// Center the string according to the bounds of its enclosing rectangle.
		double x = (getWidth() - bounds.getWidth()) / 2;
		double y = (getHeight() - bounds.getHeight()) / 2;
		double ascent = -bounds.getY();
		y += ascent;
		
		displayWord.setX((int)x);
		displayWord.setY((int)y);
		
		g2.setColor(Color.white);
		g2.drawString(displayWord.getWord(), displayWord.getX(), displayWord.getY());
	}
	
	// This will be called if the user's input thus far matches the word displayed.
	// Draw the user's input in color on top of the displayed word.
	// Length of userInput is guaranteed to be less than or equal to the length 
	// of the display word when this method is called.
	private void drawPartiallyColoredWord(Graphics2D g2)
	{
		String matchingSubstring = displayWord.getWord().substring(0, userInput.length());
		
		// fun colors
		Color lightBlue = new Color(52, 198, 235);
		Color purple = new Color(131, 52, 235);
		Color darkMagenta = new Color(176, 28, 139);
		
		g2.setColor(darkMagenta);
		g2.drawString(matchingSubstring, displayWord.getX(), displayWord.getY());
	}
	
	private void processKeyTyped(char c)
	{
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
		
		
		if (userInput.equals(displayWord.getWord()))
		{
			numericAccuracyLabel.setText(getAccuracyString());
			incrementScore(displayWord.getWord().length());
			userInput = "";
			displayNewWord();
		}
	}
	
	
	private void processKeyPressed(char c)
	{
		if (allowBackspacing)
		{
			if (c == KeyEvent.VK_BACK_SPACE && userInput.length() > 0)
			{
				userInput = userInput.substring(0, userInput.length()-1);
				repaint();
				incrementErrors();
			}
		}
	}
	
	// Overlay a white semi-transparent rectangle on the background image to make it brighter.
	private void makeImageBrighter(Graphics2D g2)
	{
		Rectangle2D whiteScreen = new Rectangle(getWidth(), getHeight());
		
		int transparency = (int)(0.2*255);
		Color whiteTransparent = new Color(255, 255, 255, transparency);

		g2.setColor(whiteTransparent);
		g2.fill(whiteScreen);
		g2.draw(whiteScreen);
	}
	
	// Compare the user input with the display word
	private boolean inputMatches()
	{
		if (userInput.length() > displayWord.getWord().length())
			return false;
		
		String s = displayWord.getWord().substring(0, userInput.length());
		
		return userInput.equals(s);
	}
	
	// check to ensure that the key typed is a letter, number, symbol, or space before appending it to wordTyped
	// i.e. exclude backspace characters, enter, shift, etc 
	// TODO may need to move this to the keypressed method, maybe call on VK constants instead of Character methods
	private boolean isValidKey(char c)
	{
		return validChars.contains(c);
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
}

