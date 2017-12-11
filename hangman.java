package hangman;

import javax.swing.JOptionPane;

import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;

public class hangman {
	

	public static void main(String[] args) throws FileNotFoundException {
		String wordToGuess = new String("");
	
		
		while (true) {	
			Dictionary Words = new Dictionary();
			wordToGuess = Words.getRandomWord();
			Game game = new Game();
			game.play(wordToGuess);
		}
	}
}

class Game {
	String[] lettersGuessed = new String[50];
	int lettersGuesedNextSpot = 0;
	private String wordGuessed = new String();
	private String word2Guess = new String();
	
	private String lastAttempt = new String();
	private int lastAttemptAmt = 0;
	private boolean lastAttemptSuccessful = false;
	
	private int misses = 0;
	private int missesAllowed = 5;
	
	boolean firstTimeThru = true;
	
	
	Game () {
		
	}
	
	boolean findMatching(int[] list, int item) {
		for (int i = 0; i < Array.getLength(list); i++) {
			if (item == list[i]) {
				return true;
			}
		}
		return false;
	}
	
	String generateNewUncoveredWord(int[] placesToChange, String letter) {
		String newWordGuessed = new String("");
		boolean changeMade = false;
		
		lastAttemptAmt = 0;
		
		for (int i = 0; i < wordGuessed.length(); i++) {
			if (wordGuessed.charAt(i) == '-' && findMatching(placesToChange, i)) {
				newWordGuessed += letter;
				lastAttemptAmt++;
				changeMade = true;
			} else {
				newWordGuessed += wordGuessed.charAt(i);
			}
		}
		lastAttempt = letter;
		lastAttemptSuccessful = true;
		if (changeMade == false) {
			lastAttemptSuccessful = false;
			misses++;
		}
		
		
		
		return newWordGuessed;
	}
	
	String checkGuess(String guessedLetter) {
		
		int[] placesToChange = new int[40];
		int placesToChangeTracker = 0;
		
		for (int i = 0; i < placesToChange.length; i++) {
			placesToChange[i] = -1;
		}

		for (int i = 0; i < word2Guess.length(); i++) {
			
			if (word2Guess.charAt(i) == guessedLetter.charAt(0)) {
				placesToChange[placesToChangeTracker] = i;
				placesToChangeTracker++;
			}
		}
		
		
		
		wordGuessed = generateNewUncoveredWord(placesToChange, guessedLetter);
		
		return wordGuessed;
	}
	
	
	
	boolean duplicateFound(String letter) {
		
		for (int i = 0; i < lettersGuesedNextSpot; i++) {
			if (letter.charAt(0) == lettersGuessed[i].charAt(0)) {
				System.out.println(lettersGuessed[i].charAt(0));
				return true;
			}
		}
		return false;
		
	}
	
	String generateHiddenWord(String word) {
		String finalWord = new String("");
		for (int i = 0; i < word.length(); i++) {
			finalWord += "-";
		}
		return finalWord;
	}
	
	boolean wordDiscovered(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == '-') {
				return false;
			}
		}
		return true;
	}
	
	void guess() {
		String answer = new String();
		
		if (firstTimeThru) {
			answer = JOptionPane.showInputDialog("Word to guess: " + wordGuessed + "\nGuess a letter:");
		} else {
			if (lastAttemptSuccessful == true) {
				answer = JOptionPane.showInputDialog("There was "+ lastAttemptAmt + " " + lastAttempt + " \nMisses: " + misses + "\n" + wordGuessed + "\nGuess a letter:");
			} else {
				answer = JOptionPane.showInputDialog("There wasn't a " + lastAttempt + " \nMisses: " + misses + "\n" + wordGuessed + "\nGuess a letter:");
			}
		}
		
		answer = answer.toLowerCase();
		if (answer.length() == 1) {
			if (Integer.valueOf(answer.charAt(0)) >= Integer.valueOf('a') || Integer.valueOf(answer.charAt(0)) >= Integer.valueOf('z') ) {	
				if (!duplicateFound(answer)) {
					
					lettersGuessed[lettersGuesedNextSpot] = answer;
					System.out.println(lettersGuessed[lettersGuesedNextSpot]);
					lettersGuesedNextSpot++;
					
					checkGuess(answer);
				} else {
					JOptionPane.showMessageDialog(null, "You already guessed that letter, silly!");
				}
		 	} else {
		 		JOptionPane.showMessageDialog(null, "You can't guess numbers!");
			}
		} else {
			JOptionPane.showMessageDialog(null, "You can only answer one letter at a time");
		}
	}
	
	void play (String wordToGuess) {
		word2Guess = wordToGuess;
		wordGuessed = generateHiddenWord(wordToGuess);
				
		
		JOptionPane.showMessageDialog(null, "Welcome to Hangman. \nYou are allowed "+ missesAllowed + " wrong guesses. \nThe word has " + wordGuessed.length() + " letters");
		
		while (true) {
			if (wordDiscovered(wordGuessed)) {
				JOptionPane.showMessageDialog(null, "You won! \nThe word is " + word2Guess +"!" );
				return;
			}
			if (misses >= missesAllowed) {
				JOptionPane.showMessageDialog(null, "You lost! \nYou had " + misses + " misses!" );
				return;
			}
			guess();
			
			firstTimeThru = false;
				
		}
	}
}


class Dictionary{
	int maxWords = 300;
	int endOfListIndex = 0;
	private String dictionarySrc = "/home/student/student/hangman";	
	private String[] dictionary = new String[maxWords];
	
	
	Dictionary() throws FileNotFoundException{
		File dictFile = new File(dictionarySrc);
		Scanner dictScan = new Scanner(dictFile);
		for (int i = 0; dictScan.hasNextLine(); i++) {
			dictionary[i] = dictScan.nextLine();
			endOfListIndex = i;
		}
		
		
	
	}
	
	String getRandomWord() {
		Random rand = new Random();
		//System.out.println(dictionary.length);
		return(dictionary[rand.nextInt(endOfListIndex + 1)]);
	}
}
