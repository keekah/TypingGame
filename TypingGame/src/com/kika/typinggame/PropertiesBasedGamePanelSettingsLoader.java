package com.kika.typinggame;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
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
		
		File directory = new File(directoryName);
		
		File [] files = directory.listFiles();
		
		Properties properties = new Properties();
		
		
		for (File f : files)
		{
			try
			{
				try (InputStream istream = new FileInputStream(f))
				{
					properties.load(istream);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
				String name = properties.getProperty("name");
				
				String backgroundImageFilename = properties.getProperty("background-image");
				Image image = Toolkit.getDefaultToolkit().createImage(backgroundImageFilename);
		
				String fontFilename = properties.getProperty("font");
				Font font = createFont(fontFilename);
				
				String wordBankFilename = properties.getProperty("word-bank-file");
				List<String> wordBank = createWordBank(wordBankFilename);
				
				
				System.out.println("Creating new GamePanelSettings object:");
				System.out.println(name + ", " + backgroundImageFilename + ", " + fontFilename + ", " + wordBankFilename + "\n");
				
				GamePanelSettings settings = new GamePanelSettings(name, image, font, wordBank);
				settingsList.add(settings);
			}
			catch (Exception e)
			{
				// If anything goes wrong while reading this file, skip it and move on
				e.printStackTrace();
			}
			
		}
		
		
		return settingsList;
	}
	
	private Font createFont(String filename)
	{
		Font font = null;
		
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new File(filename)).deriveFont(36f);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (FontFormatException e)
		{
			e.printStackTrace();
		}
		
		if (!GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font))
			System.out.println("Font not registered correctly: " + filename);
		
		return font;
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

	
	public static void main(String[] args)
	{
		new PropertiesBasedGamePanelSettingsLoader("game-settings").load();
	}
}
