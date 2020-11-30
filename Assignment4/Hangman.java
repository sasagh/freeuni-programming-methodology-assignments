import acm.program.ConsoleProgram;

import java.util.Random;

public class Hangman extends ConsoleProgram {
    private static final int LIVES = 8;
    private static int remainedLives = LIVES;
    private static int remainedCharactersToGuess;

    private static String word;
    private static boolean[] wordCharactersEntered;
    private static final Random gen = new Random();

    public void run() {
        println("Welcome to hangman!\n");

        initializeWord();

        while(true){
            if(remainedLives == 0){
                endGame(false);
                break;
            }

            if(remainedCharactersToGuess == 0){
                endGame(true);
                break;
            }

            printInfo();
            char c = readChar();

            checkCharacter(c);
        }
    }

    private void endGame(boolean win){
        if(win){
            println("You guessed the word: " + word);
            println("You win.");
        }else{
            println("You're completely hung.");
            println("The word was: " + word);
            println("You lose.");
        }
    }

    private void checkCharacter(char c){
        if(wordContainsCharacter(c)){
            println("The guess is correct");
            guessed(c);
        }else{
            println("There is no " + c + "'s in the word");
            remainedLives--;
        }
    }

    private void guessed(char c){
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i) == c && !wordCharactersEntered[i]) {
                wordCharactersEntered[i] = true;
                remainedCharactersToGuess--;
            }
        }
    }

    private void printInfo(){
        print("The word now looks like this: ");

        for(int i=0; i<word.length(); i++){
            if(wordCharactersEntered[i]){
                print(word.charAt(i));
            }else{
                print('-');
            }
        }
        println("\nYou have " + remainedLives + " guesses left.");
    }

    private char readChar() {
        while(true){
            print("Your guess: ");
            String str = readLine();

            if(str.length() != 1){
                println("Enter character, not a word or sentence or empty line");
                continue;
            }

            if(isInAlphabet(str.charAt(0))){
                return Character.toUpperCase(str.charAt(0));
            }else{
                println("Enter correct character");
            }
        }
    }

    private boolean wordContainsCharacter(char c){
        return word.indexOf(c) != -1;
    }

    private void initializeWord() {
        //int number = gen.nextInt();
        //word = new HangmanLexicon().getWord(number);
        word = "COMPUTER";
        wordCharactersEntered = new boolean[word.length()];
        remainedCharactersToGuess = word.length();
    }

    private boolean isInAlphabet(char c){
        c = Character.toUpperCase(c);
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        return alphabet.indexOf(c) != -1;
    }
}