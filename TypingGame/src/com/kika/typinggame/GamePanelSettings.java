package com.kika.typinggame;

import java.awt.*;
import java.util.List;


public class GamePanelSettings
{
	private final String name;
	private final Image backgroundImage;
	private final Font font;
	private final List<String> wordBank;


	public GamePanelSettings(String name, Image backgroundImage, Font font, List<String> wordBank)
	{
		this.name = name;
		this.backgroundImage = backgroundImage;
		this.font = font;
		this.wordBank = wordBank;
	}


	public String getName()
	{
		return name;
	}


	public Image getBackgroundImage()
	{
		return backgroundImage;
	}


	public Font getFont()
	{
		return font;
	}


	public List<String> getWordBank()
	{
		return wordBank;
	}

}
