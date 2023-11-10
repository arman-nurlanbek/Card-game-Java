import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

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


        System.out.println(log());

        activeGame = true;
    }

    public String log() {
        String res = "";
        for (Player player : players) {
            res += player.log() + "\n";
        }
        for (CardDeck deck : decks) {
            res += deck.log() + "\n";
        }
        return res;
    }

    public synchronized void winReport(Player player) {
        // TODO
        System.out.println("WIN!!! " + player);

        activeGame = false;
    }

    public synchronized void turn(Player player) {
        // TODO
        int id = player.getPlayerId();

        int fromDeck = id;
        int toDeck = id+1;
        if (toDeck > decks.size()) {
            toDeck = 1;
        }

        player.turn(decks.get(fromDeck), decks.get(toDeck));

    }

    public void startGame() {
        for (Player player : players) {
            Thread thread = new Thread(player);
            thread.start();
        }

        while (isActiveGame()) {
            try {
                Thread.sleep(3L * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("=== Game stat:");

        for (Player player : players) {
            System.out.println(player.logActions());
        }

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

        CardGame game = null;
        String inputFileName = "four.txt"; //null;
        while (game == null) {
//            System.out.println("Please enter location of pack to load:");
//            inputFileName = in.nextLine();

            String data = null;
            try {
                data = GameTools.loadTextFile(inputFileName);

                game = new CardGame(n);
                game.loadInputData(data);
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

        // start game
        game.startGame();

    }
}
