package com.kika.typinggame;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class Utilities
{

	public static Font loadFont(String filename)
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
		
		return font;
	}

}
