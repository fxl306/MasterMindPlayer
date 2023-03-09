/**
 *  This class represents the MasterMind game.
 *  The application creates a secret code of X digits. Each digit must be between Y and Z. The code breaker (user) gets M chances to guess the code.
 *  The code breaker will enter their guess at the command line when prompted (ex: 1234).
 *  The application will then respond with some number of +’s and -’s where the +’s are always printed before the -’s.
 *  If the code breaker guesses correctly, the program prints “You solved it!”.
 *  If the code breaker runs out of tries, the program prints “You lose :(“
 *  Note: X, Y, Z, M can be defined by user, or by default X =4, Y= 1, Z = 6 and M =12.
 *  @auther : Feng Long (fxl306@case.edu)
 *  @Date: 03/08/2023
 */

import java.security.SecureRandom;
import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class MasterMind {

    /*
        Stores the secret code.
        Key: the digit of the secret code.
        Value:the index of the digit.
        Each pair of Key-Value contains the digit and its corresponding index.
     */
    private Multimap<Integer, Integer> secretCode;
    private boolean ifDuplicatedCode;
    private int codeLength;
    private int codeLowerBound;
    private int codeUpperBound;
    private int guessChances;
    private int guessCounter;

    /**
     * The Constructor to build the game. The guess counter is 0 by default.
     * @param ifDuplicatedCode If duplicated digits in the secret code.
     * @param codeLength The length of the secret code.
     * @param codeLowerBound The lower bound of the secret code.
     * @param codeUpperBound The upper bound of the secret code.
     * @param guessChances The attempts allowed to guess the secret code.
     */
    public MasterMind(boolean ifDuplicatedCode, int codeLength, int codeLowerBound, int codeUpperBound, int guessChances) {
        this.ifDuplicatedCode = ifDuplicatedCode;
        this.codeLength = codeLength;
        this.codeLowerBound = codeLowerBound;
        this.codeUpperBound = codeUpperBound;
        this.guessChances = guessChances;
        this.guessCounter = 0;
    }

    /**
     * Entrance of the Game.
     */
    public void startGame () {
        System.out.println("Let's started!");

        try {
            codeMaker();
        } catch (IllegalArgumentException e) { //For non-duplicated digits, check if code length is within range of digits.
            System.out.println(e.getMessage());
            System.exit(0);
        }

        Scanner typeIn = new Scanner(System.in);
        boolean isWinner = false;
        while (guessCounter < guessChances){
            System.out.printf("Attempt %d: ", guessCounter +1);

            String answer = typeIn.nextLine();
            Result result = examGuess(answer);

            if(result.ifPerfectGuess) {
                isWinner = true;
                break;
            }
            if (!result.getScoreInString().isEmpty()){ //If gained a score.
                System.out.println(result.getScoreInString());
            } else if (!result.getNoteOfResult().isEmpty()){ // If error occurs.
                System.out.println(result.getNoteOfResult());
            }
        }

        System.out.println(isWinner ? "You solved it!" : "You lose :(");


    }

    /**
     * Generator of the secret code.
     */
    private void codeMaker () {
        if (!ifDuplicatedCode){
            if ((codeUpperBound - codeLowerBound) < codeLength){
                throw new IllegalArgumentException ("No duplicates in the digits, code length must be smaller the code range!");
            }
        }

        secretCode = HashMultimap.create();
        for (int i =0; i < codeLength; i ++) {
            int codeDigit = codeLowerBound + new SecureRandom().nextInt(codeUpperBound - codeLowerBound + 1);
            if (!ifDuplicatedCode){
                while (secretCode.containsKey(codeDigit)) { // Replace the duplicated digit.
                    codeDigit = codeLowerBound + new SecureRandom().nextInt(codeUpperBound - codeLowerBound + 1);
                }
            }
            secretCode.put(codeDigit,i);
        }
        // System.out.println(secretCode); // For testing.

    }

    /**
     * Tester of the submitted guess.
     * @param guess Input of the guess.
     * @return The result of the submitted guess, either a score or error message.
     */
    private Result examGuess (String guess){
        guessCounter++;
        if (guess.length() != codeLength){
            return new Result(String.format("Length of digits must be %d.", codeLength));
        }

        try {
            return finalScoreCalculator(guess); // Collect the score of the guess.
        } catch (IllegalArgumentException e){
            return new Result(e.getMessage());
        }
    }

    /**
     * Score Calculator of the submitted guess with all digits.
     * @param guess Input of the guess.
     * @return The final sore of the submitted guess:
     *          a string with '+' (perfect match), '-' (right digit but wrong index) and '' (wrong guess).
     */

    private Result finalScoreCalculator (String guess){
        HashMap<Integer, Integer> rightGuessCounter = new HashMap<>(); // Record how many digits were matched.
        int plusCounter = 0;
        int minusCounter = 0;
        boolean ifPerfectGuess = true;

        for (int i = 0; i < guess.length(); i ++){
            int guessDigit = Character.getNumericValue(guess.charAt(i)); // Record each digit in real time.
            if(!ifGuessWithinRange(guessDigit)){
                throw new IllegalArgumentException (String.format("Input must be digits within the range between %d and %d.", codeLowerBound, codeUpperBound));
            } else {
                int currentRightDigits = rightGuessCounter.getOrDefault(guessDigit, 0); // # of digits matched in real time.
                int totalMatchedDigits = secretCode.get(guessDigit).size(); // The total # digits matched from the guess.
                if (totalMatchedDigits > 0 && currentRightDigits == totalMatchedDigits) { //If current digit has been matched all, skip this iteration.
                    ifPerfectGuess = false;
                    continue;
                    }

                switch (examGuessDigit(guessDigit, i)) {
                    case '+': // Update + score for current digit and record its # of matches.
                        plusCounter++;
                        rightGuessCounter.put(guessDigit, rightGuessCounter.getOrDefault(guessDigit, 0) + 1);
                        break;
                    case '-':
                        minusCounter++; // Update - score for current digit and record its # of matches.
                        rightGuessCounter.put(guessDigit, rightGuessCounter.getOrDefault(guessDigit, 0) + 1);
                    default: // No match
                        ifPerfectGuess = false;
                }

            }
        }

        return new Result(scoreInString(plusCounter,minusCounter),ifPerfectGuess);


    }

    /**
     * Score calculator of each individual digit of the guess
     * @param guessDigit Individual digit of the guess
     * @param guessDigitIndex Index of the individual digit of the guess
     * @return The score of each individual digit of the guess:
     *         '+' if right digit and index;
     *         '-' if right digit but wrong index;
     *         '' if nothing is right.
     */
    private char examGuessDigit (int guessDigit, int guessDigitIndex ){
        char score = '\0';

        if (secretCode.containsKey(guessDigit)){ // If guess digit presents in the secret code.
            Collection <Integer> guessDigitPool = secretCode.get(guessDigit);
            if (guessDigitPool.contains(guessDigitIndex)){ // Right index.
                score = '+';
            } else // Wrong index.
                score = '-';
        }

        return score;
    }

    /**
     * A class to format the result including its score, error message and if-perfect-answer.
     */
    private class Result {
        private String scoreInString;
        private String noteOfResult;
        private boolean ifPerfectGuess ;

        /**
         * Constructor to initiate the class with default values.
         */
        public Result() {
            this.scoreInString = "";
            this.noteOfResult = "";
            this.ifPerfectGuess = false;
        }

        /**
         * Constructor to define the result with its score, error message and if-perfect-answer.
         * @param scoreInString The score of the guess, in String.
         * @param noteOfResult The error message of the result.
         * @param ifPerfectGuess If the result is a perfect match.
         */
        public Result(String scoreInString, String noteOfResult, boolean ifPerfectGuess) {
            this.scoreInString = scoreInString;
            this.noteOfResult = noteOfResult;
            this.ifPerfectGuess = ifPerfectGuess;
        }

        /**
         * Constructor to define the result with its score, and if-perfect-answer.Its error message is null.
         * @param scoreInString The score of the result, in String.
         * @param ifPerfectGuess If the result is a perfect match.
         */
        public Result(String scoreInString, boolean ifPerfectGuess) {
            this.scoreInString = scoreInString;
            this.ifPerfectGuess = ifPerfectGuess;
            this.noteOfResult = "";

        }

        /**
         * Constructor to define the result with error message. Its score is null, and if-perfect-answer is false.
         * @param noteOfResult The error message of the result.
         */
        public Result(String noteOfResult) {
            this.noteOfResult = noteOfResult;
            this.scoreInString = "";
            this.ifPerfectGuess = false;
        }

        /**
         * Getter method of the score of the result.
         * @return The score of the result.
         */
        public String getScoreInString() {
            return scoreInString;
        }

        /**
         * Getter method of the error message result.
         * @return the error message result.
         */
        public String getNoteOfResult() {
            return noteOfResult;
        }
    }


    /**
     * Check if the digit of the guess is within the range of digit in the secret code.
     * @param guess The input submitted guess.
     * @return True if the digit of the guess is within the range of digit in the secret code, false else.
     */
    private boolean ifGuessWithinRange (int guess) {
        return guess <= codeUpperBound && guess >= codeLowerBound;
    }


    /**
     * A string convertor that transfer the num of '+' and '-' into string with '+' and '-'.
     * @param plusCounts Number of '+' gained in score.
     * @param minusCounts Number of '-' gained in score.
     * @return The final score in string with '+' and '-'
     */
    private String scoreInString (int plusCounts, int minusCounts){
        char [] scoreString = new char [plusCounts + minusCounts];
        Arrays.fill(scoreString, 0, plusCounts, '+');
        Arrays.fill(scoreString,  plusCounts, scoreString.length, '-');
        return new String (scoreString);
    }

}
