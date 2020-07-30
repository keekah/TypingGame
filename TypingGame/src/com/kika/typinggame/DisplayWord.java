package com.kika.typinggame;

// a word that is displayed for the user to type
// includes word's location so the text color can be changed

public class DisplayWord
{
	private String word;
	private int x;				// location of the word in pixels
	private int y;				// (x,y) being the top left corner of bounding rectangle
	
	DisplayWord(String word)
	{
		this.word = word;
		x = 0;
		y = 0;
	}
	
	DisplayWord(String word, int x, int y)
	{
		this.word = word;
		this.x = x;
		this.y = y;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
}
