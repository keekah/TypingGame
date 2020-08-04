package com.kika.typinggame;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class RandomStringsLoader implements GamePanelSettingsLoader
{

	@Override
	public List<GamePanelSettings> load()
	{
		List<GamePanelSettings> settingsList = new ArrayList<GamePanelSettings>();
		
		String name = "Random Strings";
		
		Image image = Toolkit.getDefaultToolkit().createImage("backgrounds/abstract-lines.jpg");
		
		Font font = Utilities.loadFont("fonts/bitstream.ttf");
		
		List<String> wordBank = createWordBank(1000);
		
		settingsList.add(new GamePanelSettings(name, image, font, wordBank));
		
		return settingsList;
	}
	
	
	// Creates a word bank of randomly generated strings with lengths between 3 and 8, inclusive
	private List<String> createWordBank(int capacity)
	{
		List<String> wordBank = new ArrayList<String>();
		
		StringGenerator generator = new StringGenerator();
		
		for (int i = 0; i < capacity; i++)
			wordBank.add(generator.getRandomString());
		
		return wordBank;
	}

}
