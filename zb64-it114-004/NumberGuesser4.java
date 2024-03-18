import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class NumberGuesser4 {
    private int maxLevel = 1;
    private int level = 1;
    private int strikes = 0;
    private int maxStrikes = 5;
    private int number = -1;
    private boolean pickNewRandom = true;
    private Random random = new Random();
    private String fileName = "ng4.txt";
    private String[] fileHeaders = { "Level", "Strikes", "Number", "MaxLevel" };// used for demo readability
    //zb64 2/12/24
    private boolean hintUsed = false;
    private int range = 0;

    private void saveState() {
        String[] data = { level + "", strikes + "", number + "", maxLevel + "" };
        String output = String.join(",", data);
        // Note: we don't need a file reference as FileWriter creates the file if it
        // doesn't exist
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(String.join(",", fileHeaders));
            fw.write("\n");// new line
            fw.write(output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadState() {
        File file = new File(fileName);
        if (!file.exists()) {
            // Not providing output here as it's expected for a fresh start
            return;
        }
        try (Scanner reader = new Scanner(file)) {
            int lineNumber = 0;
            while (reader.hasNextLine()) {
                String text = reader.nextLine();
                // System.out.println("Text: " + text);
                if (lineNumber == 1) {
                    String[] data = text.split(",");
                    String level = data[0];
                    String strikes = data[1];
                    String number = data[2];
                    String maxLevel = data[3];
                    int temp = strToNum(level);
                    if (temp > -1) {
                        this.level = temp;
                    }
                    temp = strToNum(strikes);
                    if (temp > -1) {
                        this.strikes = temp;
                    }
                    temp = strToNum(number);
                    if (temp > -1) {
                        this.number = temp;
                        pickNewRandom = false;
                    }
                    temp = strToNum(maxLevel);
                    if (temp > -1) {
                        this.maxLevel = temp;
                    }
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {// specific exception
            e.printStackTrace();
        } catch (Exception e2) {// any other unhandled exception
            e2.printStackTrace();
        }
        System.out.println("Loaded state");
        int range = 10 + ((level - 1) * 5); //zb64 2/12/24
        System.out.println("Welcome to level " + level);
        System.out.println(
                "I picked a random number between 1-" + (range) + ", let's see if you can guess.");
    }

    /***
     * Gets a random number between 1 and level.
     * 
     * @param level (level to use as upper bounds)
     * @return number between bounds
     */
    private void generateNewNumber(int level) {
        int range = 10 + ((level - 1) * 5);
        System.out.println("Welcome to level " + level);
        System.out.println(
                "I picked a random number between 1-" + (range) + ", let's see if you can guess.");
        number = random.nextInt(range) + 1;
    }

    private void win() {
        System.out.println("That's right!");
        level++;// level up!
        strikes = 0;
    }

    private boolean processCommands(String message) {
        boolean processed = false;
        if (message.equalsIgnoreCase("quit")) {
            System.out.println("Tired of playing? No problem, see you next time.");
            processed = true;
        }
        // TODO add other conditions here
        if (message.equalsIgnoreCase("hint")) {
            if (strikes < 2) {
                System.out.println("Can't get a hint yet, try at least 2 times");
                return false;
            }
            hintUsed = true;
            int min = number - random.nextInt(4);
            int max = number + random.nextInt(4);
            if (min < 1) {
                min =1 ;
            }
            int range = 10 + ((level -1) * 5);
            if (max > range) {
                min = range;
            }
        }
        return processed;
    }

    //private void genHints(int range) {
        //nt startRange = Math.max(1, number - range/2); //this calculates the minimum range
        //int endRange = Math.min(10, number + range/2); //this caluclates the maximum range
        //System.out.println("Hint is that the number is between " + startRange + "-" + endRange);
        //hintUsed = true;
        //zb64 2/12/24
    //

    private void lose() {
        System.out.println("Uh oh, looks like you need to get some more practice.");
        System.out.println("The correct number was " + number);
        strikes = 0;
        level--;
        if (level < 1) {
            level = 1;
        }
    }

    private void processGuess(int guess) {
        if (guess <= 0) {
            return;
        }
        System.out.println("You guessed " + guess);
        if (guess == number) {
            win();
            pickNewRandom = true;
        } else {
            System.out.println("That's wrong");
            strikes++;
            if (strikes >= maxStrikes) { //this is how we know we are
                lose();
                pickNewRandom = true;
            } else {
                int diff = Math.abs(number-guess);
                if (diff <= 2) {
                    System.out.println("You are hot. You are really close");
                } else if (diff <= 5) {
                    System.out.println("You are warm! Keep going.");
                } else if (diff <= 10) {
                 System.out.println("You are cold.");
                }
                //zb64 2/12/24
            }
        }
        saveState();
    }

    private int strToNum(String message) {
        int guess = -1;
        try {
            guess = Integer.parseInt(message.trim());
        } catch (NumberFormatException e) {
            System.out.println("You didn't enter a number, please try again");
        } catch (Exception e2) {
            System.out.println("Null message received");
        }
        return guess;
    }

    public void start() {
        try (Scanner input = new Scanner(System.in);) {
            System.out.println("Welcome to NumberGuesser4.0");
            System.out.println("To exit, type the word 'quit'.");
            System.out.println("You are able to get 1 hint, type the word 'hint' to get it."); //zb64 2/12/24
            loadState();
            do {
                if (pickNewRandom) {
                    generateNewNumber(level);
                    saveState();
                    pickNewRandom = false;
                }
                System.out.println("Type a number and press enter");
                // we'll want to use a local variable here
                // so we can feed it into multiple functions
                String message = input.nextLine();
                // early termination check
                if (processCommands(message)) {
                    // command handled; don't proceed with game logic
                    break;
                }
                // this is just to demonstrate we can return a value and pass it into another
                // method
                int guess = strToNum(message);
                processGuess(guess);
                // the following line is the same as the above two lines
                // processGuess(getGuess(message));
                //if (message.equalsIgnoreCase("hint") && !hintUsed && strikes >=2) {
                    //genHints(range);
                    //continue;
                //}
                //zb64 2/12/24
            } while (true);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred. Goodbye.");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("Thanks for playing!");
    }

    public static void main(String[] args) {
        NumberGuesser4 ng = new NumberGuesser4();
        ng.start();
    }
}