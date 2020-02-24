package countLines;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

public class CountLines {

	public static void main(String[] args) {
		String data = copyFromClipboard();
		
		if(data.isEmpty() || data == null) {
			toast("Nothing in the clipboard", "Nothing here");
		}
		char maxChar = ' ';
		int maxCharCount = 0;
		int [] charCounts = new int[Character.MAX_VALUE + 1];
		int lines = 0;
		int words = 0;
		int chars = 0;
		int maxWordLen = 0;
		String bigWord = "";
		//line count
		String [] L = data.split("\n");
		for(String l : L) {
			if(l.strip().length() > 0)
				lines++;
		}
		//word frequency
		HashMap<String, Integer> map = new HashMap<>();
		String [] w = data.split("\\s");
		for(String wrd : w ) {
			if(wrd.strip().length() > 0 && onlyLetters(wrd.strip())) {
				words++;
				if(map.containsKey(wrd)) {
					map.put(wrd, map.get(wrd) + 1);
				}else {
					map.put(wrd,1);
				}
			}
		}
		Map.Entry<String, Integer> maxEntry = null;
    	for (Map.Entry<String, Integer>entry : map.entrySet())
    	{
    	    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
    	    {
    	        maxEntry = entry;
    	    }
    	}
		
		//char frequency
		for(int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);
			if(Character.isLetter(c)) {
				chars++;
				if(++charCounts[c] >= maxCharCount) {
					maxCharCount = charCounts[c];
					maxChar = c;
				}
			}
		}
			
		toast("Lines: " + lines + "\nWords: " + words + "\nCharacters: " + chars + "\nMost Used Word: " + maxEntry.getKey() + " ("+ maxEntry.getValue() +")\nMost Used Character: " + maxChar + " ("+ maxCharCount+ ") ", "Clipboard Counts");
	}

	public static boolean onlyLetters(String s) {
		s = s.trim();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			//if not whitespace or letter
			if(!Character.isLetter(c) && !Character.isWhitespace(c))
				return false;
		}
		return true;
	}
	
    public static boolean ask(Object msg, String title) {
		int x = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
		if(x == JOptionPane.CLOSED_OPTION || x == JOptionPane.CANCEL_OPTION ) {
			System.exit(0);
		}else if(x == JOptionPane.NO_OPTION) {
			return false;
		}
		return true;
	}
    
    public static void toast(Object msg, String title) {
		JOptionPane.showMessageDialog(null, msg,title,JOptionPane.OK_CANCEL_OPTION);
	}

    public static void copyToClipboard(String toCopy) {
		StringSelection ss = new StringSelection(toCopy);
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(ss,null);
	}
    
    public static String copyFromClipboard() {
		try {
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); 
			return data;
		}catch(Exception ex) {
			ex.printStackTrace();
			return "error";
		}
	}
}
