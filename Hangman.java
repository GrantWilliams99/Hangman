import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Hangman 
{
    /***********************************************
     * Variables 
     **********************************************/
    ArrayList<String>    listWords  = new ArrayList<String>();
    ArrayList<Character> word       = new ArrayList<Character>();
    ArrayList<Character> charsRight = new ArrayList<Character>();
    ArrayList<Character> charsWrong = new ArrayList<Character>();
    int numWrong = 0;
    String stringWord = "";

    /***********************************************
     * Non-Default Constructor 
     **********************************************/
    public Hangman(String filename)
    {
        // fetch the words from the category 
        readFile(filename);
        
        // choose a random word to use
        stringWord = chooseWord();

        // set up the blanks based off of the word
        for (int i = 0; i < word.size(); i++)
        {
            // add a space between _'s if needed 
            if (word.get(i) == ' ')
                charsRight.add(' ');
            else
                charsRight.add('_');
        }
    }

    /***********************************************
     * Setup
     **********************************************/
    private void readFile(String filename)
    {
        // try to open a file
        try
        {
            // create a file and scanner to read from the file 
            File words = new File(filename);
            Scanner fileReader = new Scanner(words);

            // while there are words to read, put them into the list
            while (fileReader.hasNextLine())
            {
                String word = fileReader.nextLine();
                listWords.add(word);
            }

            // remember to clean up and close everything 
            fileReader.close();
        }

        // an error if it didn't work 
        catch (FileNotFoundException error)
        {
            System.out.println("ERROR: File not found.");
        }
    }

    private String chooseWord()
    {
        // choose a random word
        Random random = new Random();
        int range = random.nextInt(listWords.size());
        String stringWord = listWords.get(range);

        // convert the string to the char ArrayList
        for (char c : stringWord.toCharArray())
            word.add(c);

        return stringWord;
    }

    public void checkGuess(char guess)
    {
        boolean isSuccess = false;

        // make the letter lowercase to prevent false flagging 
        guess = Character.toLowerCase(guess);

        // loop through word
        for (int i = 0; i < word.size(); i++)
        {
            // check the guess against each letter and put it in the right spot
            if (guess == word.get(i))
            {
                charsRight.set(i, guess);
                isSuccess = true;
            }
        }

        // an incorrect guess is put into the list and is tallied 
        if (!isSuccess)
        {
            numWrong++;
            charsWrong.add(guess);
        }
    }

    public boolean didWin()
    {
        // loop through both words, comparing them 
        for (int i = 0; i < word.size(); i++)
        {
            // if we still have a _, we're not done yet
            if (word.get(i) != charsRight.get(i))
                return false;
        }

        // we only get here if the words match
        return true;
    }

    public boolean isAlive()
    {
        // the player has only five lives 
        if (numWrong < 6)
            return true;
        return false;
    }

    /***********************************************
     * Display
     **********************************************/
    public void displayGame()
    {
        displayGallows();
        displayRight();
    }

    private void displayGallows()
    {
        System.out.println("\n  ╔══════╗    ");
        System.out.println("  ║      |    ");

        // manage the head
        if (numWrong > 0)
            System.out.println("  ║      O    ");
        else
        System.out.println("  ║         ");

        // manage the torso
        String torso = "";

        if (numWrong > 1)
            torso += "/";
        if (numWrong > 2)
            torso += "|";
        if (numWrong > 3)
            torso += "\\"; // '\' is an escape character, so two are needed 

        System.out.println("  ║     " + torso);

        // manage the legs
        String legs = "";

        if (numWrong > 4)
            legs += "/ ";
        if (numWrong > 5)
            legs += "\\";

        System.out.println("  ║     " + legs);

        // display the rest of the gallows 
        System.out.println("  ║             ");
        System.out.println("╔═╩═══════════╗");
        displayWrong();
        System.out.println("╚═════════════╝");
    }

    private void displayRight()
    {
        // iterates through the ArrayList containing the correct characters
        System.out.println("");
        for (int i = 0; i < charsRight.size(); i++)
            System.out.print(charsRight.get(i) + " ");
    }

    private void displayWrong()
    {
        // start of the incorrect bank of characters
        System.out.println("║ Wrong:      ║");
        System.out.print("║");
        int numChars = charsWrong.size();

        // fill in the bank...
        for (int i = 0; i < 13; i++)
        {
            // ... with spaces ...
            if (i % 2 == 0)
                System.out.print(" ");

            // ... and blanks ...
            else
            {
                if (numChars <= 0)
                    System.out.print("_");

                // ... unless there's a letter to print 
                else
                {
                    System.out.print(charsWrong.get(numChars - 1)); 
                    numChars--;
                }
            }
            
        }

        // finish off the bank "wall"
        System.out.println("║");
    }
}