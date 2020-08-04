package com.kika.typinggame;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;


public class PropertiesBasedGamePanelSettingsLoader implements GamePanelSettingsLoader
{
	private final String directoryName;
	
	// Load data from a directory containing properties files
	public PropertiesBasedGamePanelSettingsLoader(String directoryName)
	{
		this.directoryName = directoryName;
	}

	@Override
	public List<GamePanelSettings> load()
	{
		List<GamePanelSettings> settingsList = new ArrayList<GamePanelSettings>();
		
		File [] files = new File(directoryName).listFiles();
		
		Properties properties = new Properties();
		
		for (File f : files)
		{
			if (!f.getName().endsWith(".properties"))
				continue;
			
			try (InputStream istream = new FileInputStream(f))
			{
				properties.load(istream);
				
				String name = properties.getProperty("name");
				
				String backgroundImageFilename = properties.getProperty("background-image");
				Image image = Toolkit.getDefaultToolkit().createImage(backgroundImageFilename);
		
				String fontFilename = properties.getProperty("font");
				Font font = Utilities.loadFont(fontFilename);
				
				String wordBankFilename = properties.getProperty("word-bank-file");
				List<String> wordBank = createWordBank(wordBankFilename);
				
				settingsList.add(new GamePanelSettings(name, image, font, wordBank));
			}
			catch (Exception e)
			{
				// If anything goes wrong while reading this file, skip it and move on
				e.printStackTrace();
			}
		}
		
		return settingsList;
	}
	
	
	private List<String> createWordBank(String filename)
	{
		List<String> wordBank = new ArrayList<String>();

		try (Scanner in = new Scanner(new File(filename)))
		{
			while (in.hasNextLine())
			{
				wordBank.add(in.nextLine().trim());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return wordBank;
	}
}
