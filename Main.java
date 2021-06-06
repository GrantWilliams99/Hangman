import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main 
{
    public static int getChoiceMode(Scanner input)
    {
        // ask for player input
        System.out.println("\nWhat would you like to do?");
        int choice = 1;

        // keep asking until valid input is given 
        do 
        {
            // further instruction if input is invalid 
            if (choice != 1 && choice != 2)
                System.out.println("\nPlease enter a valid choice.");

            // choices displayed and input saved 
            System.out.println("\n  1. Play Hangman");
            System.out.println("  2. Create Custom List");
            System.out.print("\n  > ");
            choice = input.nextInt();
        }
        while(choice != 1 && choice != 2);

        // return the choice 
        return choice;

    }

    public static void playHangman(Scanner input)
    {
        boolean playAgain = true;

        while(playAgain)
        {
            // create a new instance of hangman
            String category = getChoiceCategory(input);
            Hangman hangman = new Hangman(category);

            // the main gameplay loop
            do 
            {
                hangman.displayGame();
    
                // user input handled here 
                System.out.println("\n\n  Guess a character.");
                System.out.print("\n  > ");
                String str = input.next();

                // just the first letter
                char guess = str.charAt(0);
                
                // see if it's correct 
                hangman.checkGuess(guess);
            }
            while(hangman.isAlive() && !hangman.didWin());

            // display one last time
            hangman.displayGame();

            // results 
            if (hangman.didWin())
                System.out.println("\n\n  Congratulations!");
            else
            {
                System.out.print("\n\n  Sorry, the word was ");
                System.out.println("\"" + hangman.stringWord + "\".");
                System.out.println("\n  Better luck next time!");
            }

            // asks the player if they want to play again 
            System.out.print("\n  Play again? (Y/N): ");
            String again = input.next();

            // logic for playing again 
            if (again.equals("Y") || again.equals("y"))
                playAgain = true;
            else
                playAgain = false;
        }
    }

    public static void createCustomList(Scanner input)
    {
        // try to open a file
        try
        {
            // create a file and scanner to write to the file 
            FileWriter customFile = new FileWriter("word_lists/custom.txt");

            // instructions
            System.out.println("\n  Enter in as many words as you like!");
            System.out.println("  When you're done, just enter in X.");
            String userWord = "X";

            // let the user enter in the words they want 
            do
            {
                System.out.print("\n  > ");
                userWord = input.next();

                // make sure "X" doesn't go into the list 
                if (!userWord.equals("X") && !userWord.equals("x"))
                    customFile.write(userWord + "\n");
            }
            while(!userWord.equals("X") && !userWord.equals("x"));

            // remember to clean up and close everything 
            customFile.close();
        }

        // an error if it didn't work 
        catch (IOException error)
        {
            System.out.println("ERROR: Unable to write to file.");
        }
    }

    public static String getChoiceCategory(Scanner input)
    {
        // preps for player input 
        String filename = "word_lists/";
        int choice = 1;

        // keep asking until valid input is given 
        do 
        {
            // further instruction if input is invalid 
            if (choice < 0 && choice > 6)
                System.out.println("\nPlease enter a valid choice.");

            // choices displayed and input saved 
            System.out.println("\nWhat category would you like to use?");
            System.out.println("\n  1. Animals\t  2. States");
            System.out.println("  3. Candies\t  4. Clothes");

            // only display it if there's content in the list
            if (doesCustomListExist())
                System.out.println("  5. Custom List");

            System.out.print("\n  > ");
            choice = input.nextInt();
        }
        while(choice < 0 && choice > 6);

        // appends filename based on user choice and returns it 
        switch(choice)
        {
            case 1:
                filename += "animals.txt";
                break;
            case 2: 
                filename += "states.txt";
                break;
            case 3:
                filename += "candies.txt";
                break;
            case 4:
                filename += "clothes.txt";
                break;
            case 5:
                filename += "custom.txt";
                break;
            default:
                break;
        }

        return filename;
    }

    public static boolean doesCustomListExist()
    {
        // open the file and see if there's anything in it 
        File customList = new File("word_lists/custom.txt");

        // if the list is empty (0), then this is false 
        return customList.length() != 0;
    }


    public static void main(String[] args)
    {
        // header for the game
        System.out.println("\n\n  H  A  N  G  M  A  N  ");

        // scanner is passed to avoid hiccups in opening/closing System.in 
        Scanner input = new Scanner(System.in);

        // get player choice 
        int gamemode = getChoiceMode(input);

        // send them on their way
        if (gamemode == 1)
            playHangman(input);
        else
            createCustomList(input);

        // farewell
        System.out.println("\n  Thank you for playing!\n");
        input.close();
    }
}
