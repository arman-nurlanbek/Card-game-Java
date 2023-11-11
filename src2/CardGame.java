import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardGame {

    private boolean activeGame;

    private List<Player> players;

    private List<CardDeck> decks;

    private CardGame(int playerCount) {
        players = new ArrayList<>();
        decks = new ArrayList<>();
        for (int i=1; i<=playerCount; i++) {
            players.add(new Player(i, this));
            decks.add(new CardDeck(i));
        }
    }

    public synchronized boolean isActiveGame() {
        return activeGame;
    }

    private void loadInputData(String data) {
        // TODO
        if (data == null) {
            throw new GamePlayException("Input pack is empty!");
        }

        String[] lines = data.split("\n");

        int n = players.size();
        if (lines.length != 8*n) {
            throw new GamePlayException("Input pack does not contain " + 8*players.size() + " lines!");
        }

        int[] values = new int[lines.length];
        String str;
        for (int i=0; i<lines.length; i++) {
            str = lines[i].trim();
            try {
                int val = Integer.valueOf(str);
                if (val < 0) {
                    throw new GamePlayException("Negative card denomination: " + str);
                }
                values[i] = val;
            } catch (NumberFormatException e) {
                throw new GamePlayException("Wrong card denomination: " + str);
            }
        }

        for (int i=0; i<4; i++) {
            for (int j=0; j<n; j++) {
                players.get(j).addCard(new Card(values[4*i+j]));
            }
        }

        for (int i=0; i<4; i++) {
            for (int j=0; j<n; j++) {
                decks.get(j).discardCard(new Card(values[4*n + 4*i+j]));
            }
        }


        if (LOG_DEBUG) {
            System.out.println(" <=== Initial position");
            System.out.println(log());
            System.out.println(" ===> Initial position");
        }

        activeGame = true;
    }

    protected String log() {
        String res = "";
        for (Player player : players) {
            res += player.log() + "\n";
        }
        for (CardDeck deck : decks) {
            res += deck.log() + "\n";
        }
        return res;
    }

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


    public boolean playGame() {
        for (Player player : players) {
            Thread thread = new Thread(player);
            player.doStart();
            thread.start();
        }

        long time = 0;
        while (time <= MAX_GAME_TIME && isActiveGame()) {
            try {
                Thread.sleep(SLEEP_GAME);
                time += SLEEP_GAME;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isActiveGame()) {
            if (LOG_DEBUG) {
                System.out.println("*** Time exceeded!");
            }
            activeGame = false;
            return false;
        }

        // game over
        if (LOG_DEBUG) {
            System.out.println(" <=== Game over!");
        }
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

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int n = 4; //-1;
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

        String[] files = {
                "four_e1.txt",
                "four_e2.txt",
                "four_e3.txt",
                "test3_negative.txt",
                "test2_wrong_count.txt",
                "test4_no_win.txt",
                "test1_normal.txt"
        };
        int ind = 0;
        CardGame game = null;
//        String inputFileName = "four.txt"; //null;
        String inputFileName = "test1_normal.txt";
        while (game == null) {
//            System.out.println("Please enter location of pack to load:");
//            inputFileName = in.nextLine();
            if (ind >= files.length) {
                break;
            }
            inputFileName = files[ind];
            System.out.println(inputFileName);
            ind++;

            String data = null;
            try {
                data = GameTools.loadTextFile(inputFileName);

                game = new CardGame(n);
                game.loadInputData(data);

                // start game
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
