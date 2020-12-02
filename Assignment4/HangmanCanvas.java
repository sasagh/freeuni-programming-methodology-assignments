/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

import java.awt.*;

public class HangmanCanvas extends GCanvas {

	/** Resets the display so that only the scaffold/beam/rope/hint appears */
	public void reset() {
		removeAll();
		initializeInstanceVariables();
		initializeDashedWordAndIncorrectLetters();
		drawScaffold();
	}

	/** Initialize dashed word and incorrect letters string */
	private void initializeDashedWordAndIncorrectLetters(){
		dashedWord = new GLabel("");
		dashedWord.setFont(new Font("Sans-serif", Font.BOLD ,18));
		add(dashedWord);

		incorrectLettersLabel = new GLabel("");
		add(incorrectLettersLabel);
	}

	/** Initialize instance variables */
	private void initializeInstanceVariables(){
		incorrectLetters = "Entered incorrect letters: ";
		centerX = getWidth()/2.0;
		scaffoldStartY = getHeight()/8.0;
	}

	private void drawScaffold(){
		GLine scaffold = new GLine(
				centerX-BEAM_LENGTH, scaffoldStartY,
				centerX-BEAM_LENGTH, SCAFFOLD_HEIGHT-scaffoldStartY);
		add(scaffold);
		drawBeam();
		drawRope();
	}

	private void drawBeam(){
		GLine beam = new GLine(centerX-BEAM_LENGTH, scaffoldStartY, centerX, scaffoldStartY);
		add(beam);
	}

	private void drawRope(){
		GLine rope = new GLine(centerX, scaffoldStartY, centerX, scaffoldStartY+ROPE_LENGTH);
		add(rope);
	}

	/**
	 * Updates the word on the screen to correspond to the current
	 * state of the game.  The argument string shows what letters have
	 * been guessed so far; unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		dashedWord.setLabel(word);
		dashedWord.setLocation(centerX-dashedWord.getWidth()/2, getHeight()-WORD_OFFSET);
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the
	 * user.  Calling this method causes the next body part to appear
	 * on the scaffold and adds the letter to the list of incorrect
	 * guesses that appears at the bottom of the window.
	 */
	public void noteIncorrectGuess(char c, int remainedLives) {
		incorrectLetters += c;
		incorrectLettersLabel.setLabel(incorrectLetters);
		incorrectLettersLabel.
				setLocation(centerX-incorrectLettersLabel.getWidth()/2, getHeight()-WORD_OFFSET/2.0);
		drawMan(remainedLives);
	}

	/** Draws body parts depending on guesses remaining */
	private void drawMan(int remainedLives) {
		if(remainedLives == 7) drawHead();
		else if(remainedLives == 6) drawBody();
		else if(remainedLives == 5) drawLeftArm();
		else if(remainedLives == 4) drawRightArm();
		else if(remainedLives == 3) drawRightLeg();
		else if(remainedLives == 2) drawLeftLeg();
		else if(remainedLives == 1) drawRightFoot();
		else if(remainedLives == 0) drawLeftFoot();
	}

	/** Draw the head */
	private void drawHead() {
		double x = centerX - HEAD_RADIUS;
		double y = scaffoldStartY + ROPE_LENGTH;
		GOval head = new GOval(x, y, 2*HEAD_RADIUS, 2*HEAD_RADIUS);
		add(head);
	}

	/** Draw the body */
	private void drawBody() {
		double bodyTopY = scaffoldStartY + ROPE_LENGTH + 2*HEAD_RADIUS;
		bodyBottomY = bodyTopY + BODY_LENGTH;
		GLine body = new GLine(centerX, bodyTopY, centerX, bodyBottomY);
		add(body);
	}

	/** Draw the left arm */
	private void drawLeftArm() {
		double leftArmStartX = centerX - UPPER_ARM_LENGTH;
		double upperArmY = bodyBottomY - 3.0*BODY_LENGTH/4;
		GLine leftUpperArm = new GLine(leftArmStartX, upperArmY, centerX, upperArmY);
		add(leftUpperArm);

		GLine leftLowerArm = new GLine(leftArmStartX, upperArmY, leftArmStartX, upperArmY+LOWER_ARM_LENGTH);
		add(leftLowerArm);
	}

	/** Draw the right arm */
	private void drawRightArm() {
		double rightArmStartX = centerX + UPPER_ARM_LENGTH;
		double rightUpperArmY = bodyBottomY - 3.0*BODY_LENGTH/4;
		GLine rightUpperArm = new GLine(rightArmStartX, rightUpperArmY, centerX, rightUpperArmY);
		add(rightUpperArm);

		GLine rightLowerArm =
				new GLine(rightArmStartX, rightUpperArmY, rightArmStartX, rightUpperArmY+LOWER_ARM_LENGTH);
		add(rightLowerArm);
	}

	/** Draw the left leg */
	private void drawLeftLeg() {
		GLine leftHip = new GLine(centerX, bodyBottomY, centerX - HIP_WIDTH, bodyBottomY);
		add(leftHip);

		GLine leftLeg =
				new GLine(centerX  - HIP_WIDTH, bodyBottomY, centerX  - HIP_WIDTH, bodyBottomY + LEG_LENGTH);
		add(leftLeg);
	}

	/** Draw the right leg */
	private void drawRightLeg() {
		GLine rightHip = new GLine(centerX, bodyBottomY, centerX + HIP_WIDTH, bodyBottomY);
		add(rightHip);

		GLine rightLeg =
				new GLine(centerX  + HIP_WIDTH, bodyBottomY, centerX  + HIP_WIDTH, bodyBottomY + LEG_LENGTH);
		add(rightLeg);
	}

	/** Draw the right foot */
	private void drawRightFoot(){
		double startX = centerX + HIP_WIDTH;
		double y = bodyBottomY + LEG_LENGTH;
		add(new GLine(startX, y, startX + FOOT_LENGTH, y));
	}

	/** Draw the left foot */
	private void drawLeftFoot(){
		double startX = centerX - HIP_WIDTH;
		double y = bodyBottomY + LEG_LENGTH;
		add(new GLine(startX, y, startX - FOOT_LENGTH, y));
	}


	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 30;
	private static final int BODY_LENGTH = 100;
	private static final int UPPER_ARM_LENGTH = 50;
	private static final int LOWER_ARM_LENGTH = 25;
	private static final int HIP_WIDTH = 32;
	private static final int LEG_LENGTH = 65;
	private static final int FOOT_LENGTH = 20;
	private static final int WORD_OFFSET = 75;

	/* Instance variables */
	private double bodyBottomY;
	private double centerX;
	private double scaffoldStartY;

	private GLabel dashedWord;

	private String incorrectLetters;
	private GLabel incorrectLettersLabel;
}