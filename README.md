This is application of MasterMind game implemented by Jave.

By default, it creates a secret code of 4 digits. Each digit must be between 1 and 6. The code breaker (user) gets 12 chances to guess the code. The user can set these paramenters when entering the game.

The code breaker will enter their guess at the command line when prompted (ex: 2345). 

The application will then respond with some number of +’s and -’s where the +’s are always printed before the -’s. 

- If both the digit and its index are right, will gain a '+'
- If the digit is right but the index is not, will gain a '-'
- Each digit can only be scored once. For example, a guess of 2223 against a secret code of 3232 would get +--: a plus for the second digit and a minus for the “3” and an additional minus for a “2”. One of the “2”s in the guess would not be scored because all of the “2”s in the code had already been matched.
- If the code breaker guesses correctly, the program prints “You solved it!”. 
- If the code breaker runs out of tries, the program prints “You lose :(“ 

![](https://github.com/fxl306/MasterMindPlayer/blob/main/MasterMindPlayer.gif)
