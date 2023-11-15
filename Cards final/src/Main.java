import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

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