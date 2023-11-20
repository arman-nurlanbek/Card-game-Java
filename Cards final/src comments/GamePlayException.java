public class GamePlayException extends RuntimeException {

    /**
     * Custom exception class for handling game-related exceptions.
     * This extends RuntimeException, meaning it's an unchecked exception.
     */
    public GamePlayException(String message) {
        this(message, null);
    }

    /**
     * Constructor of a GamePlayException with both a message and a cause.
     * @param message The detailed message for the exception.
     * @param cause   The cause of the exception (a throwable).
     */
    public GamePlayException(String message, Throwable cause) {
        super(message, cause);
    }

}
