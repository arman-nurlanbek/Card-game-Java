import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class GameTools {
    private static Random random = new Random();

    public static int randomInt(int number) {
        return random.nextInt(number);
    }

    public static String loadTextFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        if (encoding == null) {
            return new String(encoded);
        }
        return new String(encoded, encoding);
    }

    public static String loadTextFile(String path) throws IOException {
        return loadTextFile(path, null);
    }

    public static void saveTextFile(String path, String text) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(text);
        writer.close();
    }

}
