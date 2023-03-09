/**
 * This class represents the entrance of the MasterMind Game.
 * The application creates a secret code of X digits. Each digit must be between Y and Z. The code breaker (user) gets M chances to guess the code.
 * The user defines X, Y, Z and M, or by default X =4, Y= 1, Z = 6 and M =12.
 * @auther : Feng Long (fxl306@case.edu)
 * @Date: 03/08/2023
 */

import java.util.Scanner;
public class GamePlayer {
    private static boolean ifDuplicatedCode;
    private static int codeLength;
    private static int codeLowerBound;
    private static int codeUpperBound;
    private static int guessChances;

    public static void main(String[] args) {
         System.out.println("Welcome to the MasterMind!");
         System.out.println("You can help me choose \"any\" digits, then you can guess my digits in my mind!");

         Scanner myScanner = new Scanner(System.in);


        // Enter if enable duplicated digits.
        System.out.printf("Enter \"Y\" to enable duplicated digits, Or \"N\" to disable (if blank, default is yes):");
        while (true){
            String ifDuplicatedInput = myScanner.nextLine();
            if (ifDuplicatedInput.equalsIgnoreCase("Y") || ifDuplicatedInput.equalsIgnoreCase("")) {
                ifDuplicatedCode = true;
                break;
            }
            if (ifDuplicatedInput.equalsIgnoreCase("N")) {
                ifDuplicatedCode = false;
                break;
            } else {
                System.out.printf("Enter only \"Y\" or \"N\" to enable\\disable duplicated digits (if blank, default is Yes): ");
            }

        }

        // Enter length of the digits.
         System.out.printf("Enter length of the digits (if blank, default is 4):");
         while (true){
             String codeLengthInput = myScanner.nextLine();
             if (codeLengthInput.equals("") ||codeLengthInput.equals(" ") ) {
                 codeLength = 4;
                 break;
             } else {
                 try {
                     codeLength = checkCodeLengthInput(codeLengthInput);
                     break;
                     // invoke method;
                 } catch (IllegalArgumentException e) {
                     System.out.printf("Enter valid >=1 digits, re-enter the length of the digits (if blank, default is 4): ");
                 }
             }
         }

        // Enter lower bound of the digits.
         System.out.printf("Enter lower bound of the digits (if blank, default is 1):");
         while (true){
             String codeLowerBoundInput = myScanner.nextLine();
             if (codeLowerBoundInput.equals("") ||codeLowerBoundInput.equals(" ") ) {
                 codeLowerBound = 1;
                 break;
             } else {
                 try {
                     codeLowerBound = checkCodeLowerBoundInput(codeLowerBoundInput);
                     break;
                     // invoke method;
                 } catch (IllegalArgumentException e) {
                     System.out.printf("Enter valid digit between 0-9, re-enter the lower bound: (if blank, default is 1): ");
                 }
             }
         }

        // Enter upper bound of the digits.
         System.out.printf("Enter upper bound of the digits (if blank, the default is 6):");
         while (true){
             String codeUpperBoundInput = myScanner.nextLine();
             if (codeUpperBoundInput.equals("") ||codeUpperBoundInput.equals(" ") ) {
                 codeUpperBound = 6;
                 break;
             } else {
                 try {
                     codeUpperBound = checkCodeUpperBoundInput(codeUpperBoundInput);
                     break;
                     // invoke method;
                 } catch (IllegalArgumentException e) {
                     System.out.printf("Enter valid digit between 0-9 and > lower bound, re-enter the upper bound (if blank, the default is 6): ");
                 }
             }
         }


        // Enter num of guess attempts desired.
        System.out.printf("Enter num of guess attempts desired (if blank, the default is 12):");
        while (true){
            String guessChanceInput = myScanner.nextLine();
            if (guessChanceInput.equals("") ||guessChanceInput.equals(" ") ) {
                guessChances = 12;
                break;
            } else {
                try {
                    guessChances = checkCodeLengthInput(guessChanceInput);
                    break;
                    // invoke method;
                } catch (IllegalArgumentException e) {
                    System.out.printf("Enter valid digit between 0-9  (if blank, the default is 12): ");
                }
            }
        }

        //Initiate the game.
        MasterMind game = new MasterMind(ifDuplicatedCode,codeLength,codeLowerBound,codeUpperBound,guessChances);
        game.startGame();
        myScanner.close();

    }

    /**
     * Check the input code length is digit and not equal to 0.
     * @param str Input string of the code length.
     * @return the code length or error message.
     */
     private static int checkCodeLengthInput (String str) {
         if (Integer.parseInt(str) != 0 && str.matches("[0-9]+")) {
             return Integer.parseInt(str);
         } else {
            throw (new IllegalArgumentException("Please only choose valid >=1 digits"));
         }
     }

    /**
     * Check the input lower bound is digit within 0 - 9.
     * @param str Input string of the lower bound of the digits.
     * @return the lower bound of the digits or error message.
     */
    private static int checkCodeLowerBoundInput (String str) {
        if (Integer.parseInt(str) >=0 && Integer.parseInt(str) <=9 ) {
            return Integer.parseInt(str);
        } else {
            throw (new IllegalArgumentException("Please only choose valid 0-9 digits"));
        }
    }

    /**
     * Check the input upper bound is digit within 0 - 9 and greater than lower bound.
     * @param str Input string of the lower bound of the digits.
     * @return the lower bound of the digits or error message.
     */
    private static int checkCodeUpperBoundInput (String str) {
        if (Integer.parseInt(str) >=0 && Integer.parseInt(str) <=9 && Integer.parseInt(str) > codeLowerBound) {
            return Integer.parseInt(str);
        } else {
            throw (new IllegalArgumentException("Please only choose valid 0-9 digits and > lower bound"));
        }
    }

}
