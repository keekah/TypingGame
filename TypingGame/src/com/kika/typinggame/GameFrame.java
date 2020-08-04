package com.kika.typinggame;

import java.awt.*;
import javax.swing.*;


public class GameFrame extends JFrame
{	
	private static final long serialVersionUID = 1L;

	public GameFrame(GamePanelSettingsLoader loader)
	{
		// set the frame size according to user's screen size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		setSize(width/4, height/2);	
		setLocationRelativeTo(null);
//		setLocationByPlatform(true);


		WelcomePanel welcomeScreen = new WelcomePanel(this, loader);
		setContentPane(welcomeScreen);
	}
}