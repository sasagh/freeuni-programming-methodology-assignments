/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import acm.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HangmanLexicon {
	private final ArrayList<String> words = new ArrayList<>();

	public HangmanLexicon(){
		try{
			BufferedReader reader = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while(true){
				String line = reader.readLine();

				if(line == null) break;

				words.add(line.toUpperCase());
			}
		}catch (IOException e){
			throw new RuntimeException();
		}
	}

/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return words.size();
	}

/** Returns the word at the specified index. */
	public String getWord() {
		int index = RandomGenerator.getInstance().nextInt(0, getWordCount()-1);
		return words.get(index);
	};
}
