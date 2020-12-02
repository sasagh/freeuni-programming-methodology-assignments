import acm.program.*;

public class Hangman extends ConsoleProgram {
    private static final int LIVES = 8;
    private static int remainedLives = LIVES;
    private static int remainedCharactersToGuess;

    private static String word;
    private static boolean[] wordCharactersEntered;
    private HangmanCanvas canvas;

    public void init() {
        canvas = new HangmanCanvas();
        add(canvas);
    }

    public void run() {
        canvas.reset();
        println("Welcome to hangman!\n");

        initializeWord();

        while(true){
            displayWordOnCanvas();

            if(remainedLives == 0){
                endGame(false);
                break;
            }

            if(remainedCharactersToGuess == 0){
                endGame(true);
                break;
            }

            printInfo();

            char c = readChar("Your guess: ");
            checkCharacter(c);
        }
    }

    /** Initialize word */
    private void initializeWord(){
        word = new HangmanLexicon().getWord();
        wordCharactersEntered = new boolean[word.length()];
        remainedCharactersToGuess = word.length();
    }

    /** Display dashed word on the canvas */
    private void displayWordOnCanvas() {
        canvas.displayWord(getDashedWord());
    }

    /** Print information: how dashed word looks like and how many attempts has left */
    private void printInfo(){
        print("The word now looks like this: ");
        print(getDashedWord());
        println("\nYou have " + remainedLives + " guesses left.");
    }

    /** Read single character from console */
    private char readChar(String message) {
        while(true){
            print(message);
            String str = readLine();

            if(str.length() != 1){
                println("Enter character, not a word or sentence or empty line");
                continue;
            }

            if(isInAlphabet(str.charAt(0))) return Character.toUpperCase(str.charAt(0));
            else println("Enter correct character");
        }
    }

    /** Check if entered character is correct and reacts accordingly */
    private void checkCharacter(char c){
        if(wordContainsCharacter(c)){
            println("The guess is correct");
            guessed(c);
        }else{
            println("There is no " + c + "'s in the word");
            remainedLives--;
            canvas.noteIncorrectGuess(c, remainedLives);
        }
    }

    /** Last method of the game, prints message about winning/losing */
    private void endGame(boolean win){
        if(win){
            println("You guessed the word: " + word);
            println("You win.");
        }else{
            println("You're completely hung.");
            println("The word was: " + word);
            println("You lose.");
        }

        println();
        playAgain();
    }

    /** Asks user to play a new game */
    private void playAgain(){
        while(true){
            char playAgain = readChar("Want to play again?(y/n): ");
            if(playAgain == 'Y'){
                println();
                remainedLives = LIVES;
                run();
                break;
            }else if(playAgain == 'N'){
                println();
                break;
            }

            println("Enter correct character");
        }
    }

    /** Handle case, when the entered character is correct */
    private void guessed(char c){
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i) == c && !wordCharactersEntered[i]) {
                wordCharactersEntered[i] = true;
                remainedCharactersToGuess--;
            }
        }
    }

    /** Get dashed word at the current moment */
    private String getDashedWord(){
        StringBuilder dashedWord = new StringBuilder();

        for(int i=0; i<word.length(); i++){
            dashedWord.append(wordCharactersEntered[i] ? word.charAt(i) : '-');
        }
        return dashedWord.toString();
    }

    /** Checks if character presents in the word */
    private boolean wordContainsCharacter(char c){
        return word.indexOf(c) != -1;
    }

    /** Checks if character presents in the english alphabet */
    private boolean isInAlphabet(char c){
        c = Character.toUpperCase(c);
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        return alphabet.indexOf(c) != -1;
    }
}