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
		readPlayersNumber();
		readPlayersNames();
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		categorySelected = new boolean[nPlayers][N_CATEGORIES];
		scores = new int[nPlayers][N_CATEGORIES];
		playGame();
	}

	/** Read players number which is between 1 and 4 */
	private void readPlayersNumber(){
		while(true){
			int playersNumber = getDialog().readInt("Enter number of players");
			if(playersNumber > 0 && playersNumber < 5) {
				nPlayers = playersNumber;
				break;
			}else{
				getDialog().showErrorMessage("Please, enter values between 1 and 4");
			}
		}
	}

	/** Read players' names */
	private void readPlayersNames(){
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			while(true){
				String playerName = getDialog().readLine("Enter name for player " + i);
				if(playerName.length() > 0){
					playerNames[i - 1] = playerName;
					break;
				}else{
					getDialog().showErrorMessage("Please, enter a valid name");
				}
			}
		}
	}

	/** Play game */
	private void playGame() {
		for(int i=0; i<N_SCORING_CATEGORIES; i++){
			for(int j=1; j<=nPlayers; j++){
				playerTurn(j);
			}
		}

		calculateScores();
	}

	/** Calculate scores sum and update scores' table */
	private void calculateScores() {
		int[] upperScore = new int[nPlayers];
		int[] upperBonus = new int[nPlayers];
		int[] lowerScore = new int[nPlayers];
		int[] total = new int[nPlayers];
		int winnerIndex = 0;
		int maxScore = 0;

		for(int i=0; i<nPlayers; i++){
			for(int j=0; j<7; j++){
				upperScore[i] += scores[i][j];
				total[i] += scores[i][j];
			}

			for(int j=7; j<N_CATEGORIES; j++){
				lowerScore[i] += scores[i][j];
				total[i] += scores[i][j];
			}

			if(upperScore[i] >= 63) {
				upperBonus[i] = 35;
				total[i] += 35;
			}

			if(total[i] > maxScore){
				winnerIndex = i;
				maxScore = total[i];
			}

			display.updateScorecard(UPPER_SCORE, i+1, upperScore[i]);
			display.updateScorecard(UPPER_BONUS, i+1, upperBonus[i]);
			display.updateScorecard(LOWER_SCORE, i+1, lowerScore[i]);
			display.updateScorecard(TOTAL, i+1, total[i]);
		}

		display.printMessage(
				"Congratulations, "
						+ playerNames[winnerIndex]
						+ ", you're the winner with a total score of "
						+ maxScore
						+ "!"
		);
	}

	/** Player turn */
	private void playerTurn(int playerIndex){
		display.printMessage(playerNames[playerIndex-1] + "'s turn. Click \"Roll Dice\" button to roll the dice.");
		display.waitForPlayerToClickRoll(playerIndex);

		int[] diceNumbers = new int[N_DICE];

		for(int i=1; i<=3; i++){
			roll(diceNumbers, i);
			display.displayDice(diceNumbers);
			if(i!=3) {
				display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
				display.waitForPlayerToSelectDice();
			}
		}

		while(true){
			display.printMessage("Select a category for this roll");
			int categoryNumber = display.waitForPlayerToSelectCategory();
			int score = getScore(categoryNumber, diceNumbers);

			if(!categorySelected[playerIndex-1][categoryNumber]){
				categorySelected[playerIndex-1][categoryNumber] = true;
				scores[playerIndex-1][categoryNumber] = score;
				display.updateScorecard(categoryNumber, playerIndex, score);
				break;
			}else{
				getDialog().showErrorMessage("Please, select category which is not selected already");
			}
		}

	}

	/** Roll a dice */
	private void roll(int[] diceNumbers, int rollNumber){
		for(int i=0; i<N_DICE; i++){
			if(display.isDieSelected(i) || rollNumber == 1){
				diceNumbers[i] = rgen.nextInt(1, 6);
			}
		}
	}

	/** Returns score for specific dice combination */
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
			default -> throw new ErrorException("checkCategory: Illegal category");
		}
	}

	/** Check different combinations */
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
	private final RandomGenerator rgen = new RandomGenerator();
	private boolean[][] categorySelected;
	private int[][] scores;
}
