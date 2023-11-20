import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The class represents the Card Game.
 */
public class CardGame {

    // The boolean value which is True when a game is active and False when a game is not active.
    private boolean activeGame;

    // The list that represents a list of players in a game.
    private List<Player> players;

    // The list that represents a list of decks that are used in a game.
    private List<CardDeck> decks;

    /**
     * Constructor to create a new game session with determined number of players.
     * Constructor counts how many players and decks are in the game and creates lists of players and decks respectfully.
     * @param playerCount represents a number of players in a game.
     */
    private CardGame(int playerCount) {
        players = new ArrayList<>();
        decks = new ArrayList<>();
        for (int i=1; i<=playerCount; i++) {
            players.add(new Player(i, this));
            decks.add(new CardDeck(i));
        }
    }

    /**
     * The method retrieves a boolean variable activeGame.
     * It is true if a game is active and false if game is not active.
     * Synchronized method is used to execute a chance of modifying the variable activeGame by multiple threads at a time.
     * @return boolean variable activeGame. True or False.
     */
    public synchronized boolean isActiveGame() {
        return activeGame;
    }

    /**
     * Retrieves a player from the game based on their ID.
     * @param playerId The unique identifier of the player.
     * @return The player with the specified ID.
     * @throws GamePlayException if no player with the given ID is found.
     */
    protected Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.getPlayerId() == playerId) {
                return player;
            }
        }
        throw new GamePlayException("No such player: " + playerId);
    }

    /**
     * Retrieves a card deck from the game based on its ID.
     * @param deckId The unique identifier of the deck.
     * @return The card deck with the specified ID.
     * @throws GamePlayException if no deck with the given ID is found.
     */
    protected CardDeck getDeck(int deckId) {
        for (CardDeck deck : decks) {
            if (deck.getDeckId() == deckId) {
                return deck;
            }
        }
        throw new GamePlayException("No such deck: " + deckId);
    }

    /**
     * Loads and processes input data for initializing the game state. 
     * This includes parsing the data string, validating it, and assigning the initial cards 
     * to players and decks based on this data.
     * @param data The string data representing initial card values for players and decks.
     * @throws GamePlayException if the data is null, improperly formatted, or contains invalid values.
     */
    private void loadInputData(String data) {
        //TODO
        // Check if the input data string is null.
        if (data == null) {
            throw new GamePlayException("Input pack is empty!");
        }
        // Split the data into separate lines.
        String[] lines = data.split("\n");

        // Verify that the number of lines in the data matches the expected number based on the number of players.
        int n = players.size();
        if (lines.length != 8*n) {
            throw new GamePlayException("Input pack does not contain " + 8*players.size() + " lines!");
        }
        // Convert the string data into integer values representing card denominations.
        int[] values = new int[lines.length];
        String str;
        for (int i=0; i<lines.length; i++) {
            // Remove leading and trailing whitespace from each line.
            str = lines[i].trim();
            try {
                int val = Integer.valueOf(str);
                // Check for negative values, which are not allowed.
                if (val < 0) {
                    throw new GamePlayException("Negative card denomination: " + str);
                }
                values[i] = val;
            } catch (NumberFormatException e) {
                throw new GamePlayException("Wrong card denomination: " + str);
            }
        }

        // Distribute cards to players based on the parsed values.
        for (int i=0; i<4; i++) {
            for (int j=0; j<n; j++) {
                players.get(j).addCard(new Card(values[4*i+j]));
            }
        }
        // Distribute remaining cards to decks.
        for (int i=0; i<4; i++) {
            for (int j=0; j<n; j++) {
                decks.get(j).discardCard(new Card(values[4*n + 4*i+j]));
            }
        }

        // Log the initial game state if debugging is enabled.
        if (LOG_DEBUG) {
            System.out.println(" <=== Initial position");
            System.out.println(log());
            System.out.println(" ===> Initial position");
        }
        // Set the game as active.
        activeGame = true;
    }

    /**
     * Starts the game by setting the activeGame flag to true.
     */
    public void startGame() {
        activeGame = true;
    }

    /**
     * The method creates String with an information about actions of players and decks.
     * @return res - string with an information.
     */
    protected String logGame() {
        String res = "";
        for (Player player : players) {
            res += player.logPlayer() + "\n";
        }
        for (CardDeck deck : decks) {
            res += deck.getContents() + "\n";
        }
        return res;
    }

    /**
     * The method creates a report about a winner player.
     * @param winPlayer player that has won a game.
     * @throws GamePlayException if player has not actually won a game.
     */
    public synchronized void winReport(Player winPlayer) {
        if (!activeGame) return;

        if (!winPlayer.isWin()) {
            throw new GamePlayException("Error win report " + winPlayer);
        }
        System.out.println(winPlayer.toString() + " wins");
        activeGame = false;

        for (Player player : players) {
            if (player != winPlayer) {
                player.doWinInform(winPlayer);
            }
        }
    }

    /**
     * The method is responsible for making a turn (next move) for a player.
     * @param player Player in a game.
     */
    public synchronized void turn(Player player) {
        // TODO
        int id = player.getPlayerId();

        int fromDeck = id;
        int toDeck = id+1;
        if (toDeck > decks.size()) {
            toDeck = 1;
        }

        player.turn(decks.get(fromDeck-1), decks.get(toDeck-1));

    }

    public static long SLEEP_GAME = 2L * 1000L;
    public static long SLEEP_PLAYER = 1L * 1000L;

    public static long MAX_GAME_TIME = 30L * 1000L;

    public static boolean LOG_DEBUG = false;

    /**
     * Begins and manages the card game. It starts threads for each player, controls the game timing, 
     * and handles the game's conclusion, including logging and saving game results.
     * @return true if the game completes successfully, false if the game is terminated due to time constraints.
     * @throws GamePlayException if there is an error in saving the game logs.
     */
    public boolean playGame() {
        // Start a thread for each player and initiate their starting state.
        for (Player player : players) {
            Thread thread = new Thread(player);
            player.doStart();
            thread.start();
        }
        // Control the game timing, ensuring it doesn't exceed the maximum allowed duration.
        long time = 0;
        while (time <= MAX_GAME_TIME && isActiveGame()) {
            try {
                Thread.sleep(SLEEP_GAME);
                time += SLEEP_GAME;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Check if the game ended due to exceeding the maximum time.
        if (isActiveGame()) {
            if (LOG_DEBUG) {
                System.out.println("*** Time exceeded!");
            }
            activeGame = false;
            return false;
        }

        // Handle game over scenario.
        if (LOG_DEBUG) {
            System.out.println(" <=== Game over!");
        }
        // Process end-of-game actions for each player and save their action logs.
        for (Player player : players) {
            player.doExit();
            String log = player.logActions();
            try {
                GameTools.saveTextFile(player.logFileName(), log);
            } catch (IOException e) {
                throw new GamePlayException("Error save log file for " + player, e);
            }
            if (LOG_DEBUG) {
                System.out.println(log);
            }
        }
        // Save the final state of each deck to a log file.
        for (CardDeck deck : decks) {
            String log = deck.getContents();
            try {
                GameTools.saveTextFile(deck.logFileName(), log);
            } catch (IOException e) {
                throw new GamePlayException("Error save log file for " + deck, e);
            }
            if (LOG_DEBUG) {
                System.out.println(log);
            }
        }
        if (LOG_DEBUG) {
            System.out.println(" ===> Game over!");
        }

        return true;
    }

    /**
     * Runs a series of predefined tests for the CardGame class.
     * This method loads different game scenarios from specified files and 
     * simulates each game to validate the game logic and functionality.
     * It's useful for quickly testing multiple game setups and validating 
     * the game's response to various inputs, especially edge cases.
     */
    public static void tests() {
        Scanner in = new Scanner(System.in);

        // Initialize the number of players. The default in this context is set to 4.
        int n = 4; //-1;

        // Loop to ensure a valid number of players is entered.
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
        // Array of file names that contain different game scenarios for testing.
        String[] files = {
                "four_e1.txt",
                "four_e2.txt",
                "four_e3.txt",
                "test3_negative.txt",
                "test2_wrong_count.txt",
                "test4_no_win.txt",
                "test1_normal.txt"
        };
        // Index to iterate over the files array.
        int ind = 0; 
        // Variable to hold the CardGame instance.
        CardGame game = null;

//        String inputFileName = "four.txt"; //null;
        // Default test file name. Can be modified to test different scenarios.
        String inputFileName = "test1_normal.txt";

        // Iterate over the test files and simulate each game.
        while (game == null) {
//            System.out.println("Please enter location of pack to load:");
//            inputFileName = in.nextLine();
            if (ind >= files.length) {
                break;
            }
            inputFileName = files[ind];
            System.out.println(inputFileName);
            ind++;

            // Variable to hold the game data from the file.
            String data = null;
            try {
                // Load the game data from the file.
                data = GameTools.loadTextFile(inputFileName);
                // Create a new game instance and load the data.
                game = new CardGame(n);
                game.loadInputData(data);
                game.startGame();

                // Start the game. if it fails, reset the game variable to null.
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

    /**
     * The main method to start the card game. It prompts for game setup information, 
     * loads the game data, and initiates the game play.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Initialize the number of players.
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

        int ind = 0;
        CardGame game = null;
        while (game == null) {
            System.out.println("Please enter location of pack to load:");
            String inputFileName = in.nextLine();

            String data = null;
            try {
                data = GameTools.loadTextFile(inputFileName);

                game = new CardGame(n);
                game.loadInputData(data);
                game.startGame();

                // // Start the game; if it doesn't play successfully, reset and try with next file.
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
