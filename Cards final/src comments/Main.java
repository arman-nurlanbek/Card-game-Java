import java.io.IOException;
import java.util.Scanner;

/**
 * The main entry point for the Card Game application.
 * This method prompts the user to input the number of players and the location of the game pack (input file). 
 * It initializes and starts the Card Game based on these inputs.
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Initialize the number of players. It must be a positive integer.
        int n = -1;
        while (n <= 0) {
            System.out.println("Please enter the number of players:");
            try {
                n = Integer.valueOf(in.nextLine());
                if (n <= 0) {
                    System.out.println("Wrong number of players!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong number of players!");
                n = -1;
            }
        }
        // Initialize variables for game setup.
        int ind = 0;
        CardGame game = null;

         // Loop until a valid game setup is provided and the game starts successfully.
        while (game == null) {
            System.out.println("Please enter location of pack to load:");
            String inputFileName = in.nextLine();

            String data = null;
            try {
                // Load game data from the specified file.
                data = GameTools.loadTextFile(inputFileName);

                // Initialize the game with the number of players and load the game data.
                game = new CardGame(n);
                game.loadInputData(data);
                game.startGame();

                // Start the game; if it doesn't play successfully, reset the game variable.
                if (!game.playGame()) {
                    game = null;
                }
            } catch (IOException e) {
                System.out.println("Error load file: " + inputFileName);
                System.out.println(e);
                game = null;
            } catch (GamePlayException e) {
                System.out.println(e.getMessage());
                game = null;
            }

        }

        in.close();

    }
}