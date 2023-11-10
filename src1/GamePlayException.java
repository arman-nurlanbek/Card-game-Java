public class GamePlayException extends RuntimeException {

    public GamePlayException(String message) {
        this(message, null);
    }

    public GamePlayException(String message, Throwable cause) {
        super(message, cause);
    }

}
