/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

import java.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		categorySelected = new boolean[nPlayers][N_CATEGORIES];
		playGame();
	}

	private void playGame() {
		for(int i=0; i<N_SCORING_CATEGORIES; i++){
			for(int j=1; j<=nPlayers; j++){
				playerTurn(j);
			}
		}
	}

	private void playerTurn(int playerIndex){
		display.waitForPlayerToClickRoll(playerIndex);

		int[] diceNumbers = new int[N_DICE];

		for(int i=1; i<=3; i++){
			roll(diceNumbers, i);
			display.displayDice(diceNumbers);
			if(i!=3) display.waitForPlayerToSelectDice();
		}

		while(true){
			int categoryNumber = display.waitForPlayerToSelectCategory();
			int score = getScore(categoryNumber, diceNumbers);

			if(!categorySelected[playerIndex-1][categoryNumber]){
				categorySelected[playerIndex-1][categoryNumber] = true;
				display.updateScorecard(categoryNumber, playerIndex, score);
				break;
			}else{
				getDialog().showErrorMessage("Please, select category which is not selected already");
			}
		}

	}

	private void roll(int[] diceNumbers, int rollNumber){
		for(int i=0; i<N_DICE; i++){
			if(display.isDieSelected(i) || rollNumber == 1){
				diceNumbers[i] = rgen.nextInt(1, 6);
			}
		}
	}

	private int getScore(int categoryNumber, int[] diceNumbers){
		Arrays.sort(diceNumbers);

		switch (categoryNumber) {
			case ONES, TWOS, THREES, FOURS, FIVES, SIXES -> {
				return numbersCase(categoryNumber, diceNumbers);
			}
			case THREE_OF_A_KIND, FOUR_OF_A_KIND, YAHTZEE -> {
				return nOfAKindCase(categoryNumber, diceNumbers);
			}
			case FULL_HOUSE -> {
				return fullHouseCase(diceNumbers);
			}
			case SMALL_STRAIGHT, LARGE_STRAIGHT -> {
				return straightCase(categoryNumber, diceNumbers);
			}
			case CHANCE -> {
				return java.util.stream.IntStream.of(diceNumbers).sum();
			}
			default -> {
				throw new ErrorException("checkCategory: Illegal category");
			}
		}
	}

	private int straightCase(int categoryNumber, int[] diceNumbers){
		boolean temp = false;
		for(int i=0; i<N_DICE-1; i++){
			if(diceNumbers[i]+1 != diceNumbers[i+1]){
				if(!temp && categoryNumber == SMALL_STRAIGHT) {
					temp = true;
					continue;
				}

				return 0;
			}
		}

		return categoryNumber == SMALL_STRAIGHT ? 30 : 40;
	}

	private int fullHouseCase(int[] diceNumbers){
		if(diceNumbers[0] != diceNumbers[1]) return 0;

		if(diceNumbers[1] == diceNumbers[2]){
			return diceNumbers[3] == diceNumbers[4] ? 25 : 0;
		}else{
			return diceNumbers[2] == diceNumbers[3] && diceNumbers[3] == diceNumbers[4] ? 25 : 0;
		}
	}

	private int nOfAKindCase(int categoryNumber, int[] diceNumbers){
		int lastElementIndexToLook = 1;

		if(categoryNumber == THREE_OF_A_KIND) lastElementIndexToLook = 3;
		else if(categoryNumber == FOUR_OF_A_KIND) lastElementIndexToLook = 2;

		for (int i = 0; i < lastElementIndexToLook; i++) {
			if (diceNumbers[i] == diceNumbers[i + 1] && diceNumbers[i] == diceNumbers[i + 2]) {
				if(categoryNumber == FOUR_OF_A_KIND && diceNumbers[i] != diceNumbers[i+3]) continue;

				if(categoryNumber == YAHTZEE) {
					return diceNumbers[i] == diceNumbers[i+4] ? 50 : 0;
				}

				return java.util.stream.IntStream.of(diceNumbers).sum();
			}
		}
		return 0;
	}

	private int numbersCase(int categoryNumber, int[] diceNumbers){
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (diceNumbers[i] == categoryNumber) score += categoryNumber;
		}
		return score;
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;	
	private RandomGenerator rgen = new RandomGenerator();
	private boolean[][] categorySelected;
}
