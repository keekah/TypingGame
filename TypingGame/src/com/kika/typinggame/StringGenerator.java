package com.kika.typinggame;

public class StringGenerator
{
	// Character sets to be used in generating random characters of a certain type
	public static final String PUNCTUATION = "!?,.\"\':;-";
	public static final String BRACKETS = "{}()[]<>";
	public static final String SYMBOLS = "!@#$%^&*-_=+\\/";
	public static final String HEX = "0123456789ABCDEF";
	
	
	// Generate a random string between 3 and 8 characters (arbitrary)
	public String getRandomString()
	{
		int length = (int)(Math.random() * 6) + 3;
		
		return getRandomString(length);
	}
	
	public String getRandomString(int length)
	{
		if (length < 0)
			throw new IllegalArgumentException();
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < length; i++)
			sb.append(getRandomChar());
		
		return sb.toString();
	}
	
	// Generate a random string with length between min, inclusive, and max, inclusive
	public String getRandomString(int min, int max)
	{
		if (min < 0)
			throw new IllegalArgumentException("min");
		
		int numPossibleLengths = max-min+1;
		int length = (int)(Math.random() * numPossibleLengths) + min;
		
		return getRandomString(length);
	}
	
	// Returns the character for a given ASCII value between 33 (!) and 126 (~). 
	// i.e. any number, letter, symbol, or punctuation mark
	public char getRandomChar()
	{
		int ascii = (int)(Math.random() * 93) + '!';
		
		return (char)ascii;
	}
	
	// Get a random character from a given list of characters
	public char getRandomChar(String validChars)
	{
		int index = (int)(Math.random() * validChars.length());
		
		return validChars.charAt(index);
	}
	
	// Get a character A-Z or a-z, no accented characters
	public char getAlphabeticChar()
	{
		int number = (int)(Math.random() * 2);
		
		if (number == 0)
			return getUpperAlphabeticChar();
		
		return getLowerAlphabeticChar();
	}
	
	// A-Z, no accented characters
	public char getUpperAlphabeticChar()
	{
		int ascii = (int)(Math.random() * 26) + 'A';
		
		return (char)ascii;
	}
	
	// a-z, no accented characters 
	public char getLowerAlphabeticChar()
	{
		int ascii = (int)(Math.random() * 26) + 'a';

		return (char)ascii;
	}
	
	public char getNumericChar()
	{
		int number = (int)(Math.random() * 10);
		
		return String.valueOf(number).charAt(0);
	}
	
	public char getSymbolChar()
	{
		return getRandomChar(SYMBOLS);
	}
	
	public char getPunctuationChar()
	{
		return getRandomChar(PUNCTUATION);
	}
	
	public char getBracket()
	{
		return getRandomChar(BRACKETS);
	}
	
	public String getHexLiteral()
	{
		int length = (int)(Math.random() * 16) + 1;

		StringBuilder sb = new StringBuilder();
		sb.append("0x");
		
		for (int i = 0; i < length; i++)
			sb.append(getRandomChar(HEX));
		
		// Append an L sometimes, just for fun
		if (length % 2 == 0)
			sb.append("L");
		
		return sb.toString();
	}
	
}
