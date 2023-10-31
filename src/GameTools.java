import java.util.Random;

public class GameTools {
    private static Random random = new Random();

    public static int randomInt(int number) {
        return random.nextInt(number);
    }
}
