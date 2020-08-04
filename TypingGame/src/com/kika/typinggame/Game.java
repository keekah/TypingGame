package com.kika.typinggame;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Game
{
	public static void main(String [] args)
	{
		EventQueue.invokeLater(new Runnable()
			{
				public void run()
				{
					List<GamePanelSettingsLoader> loaders = new ArrayList<GamePanelSettingsLoader>();
					loaders.add(new PropertiesBasedGamePanelSettingsLoader("game-settings"));
					loaders.add(new RandomStringsLoader());
					
					GameFrame frame = new GameFrame(new CompositeLoader(loaders));
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
			});
	}
}