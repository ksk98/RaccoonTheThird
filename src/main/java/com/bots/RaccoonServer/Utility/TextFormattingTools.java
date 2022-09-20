package com.bots.RaccoonServer.Utility;

public abstract class TextFormattingTools {
    public static String padTextWithSpacesFromLeft(String text, int finalLength) {
        if (finalLength < text.length())
            return text;

        return " ".repeat(finalLength - text.length()) + text;
    }
}
