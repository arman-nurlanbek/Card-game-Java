import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Utility class providing methods for common game-related operations.
 */
public class GameTools {
    private static Random random = new Random();

    /**
     * Generates a random integer.
     * @param number The upper bound (exclusive) for the random number.
     * @return A random integer between 0 (inclusive) and the specified number (exclusive).
     */
    public static int randomInt(int number) {
        return random.nextInt(number);
    }

    /**
     * Loads the contents of a text file into a string.
     * @param path     The file path of the text file.
     * @param encoding The charset encoding to use. If null, uses the default encoding.
     * @return The contents of the file as a string.
     * @throws IOException If an I/O error occurs.
     */
    public static String loadTextFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        if (encoding == null) {
            return new String(encoded);
        }
        return new String(encoded, encoding);
    }

    /**
     * Overloaded method to load a text file using the default encoding.
     * @param path The file path of the text file.
     * @return The contents of the file as a string.
     * @throws IOException If an I/O error occurs.
     */
    public static String loadTextFile(String path) throws IOException {
        return loadTextFile(path, null);
    }

    /**
     * Saves a string as text to a file.
     * @param path The file path where the text will be saved.
     * @param text The string to be saved.
     * @throws IOException If an I/O error occurs during writing.
     */
    public static void saveTextFile(String path, String text) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(text);
        writer.close();
    }

}
