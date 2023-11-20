import java.util.ArrayList;
import java.util.List;

/**
 * The class is used to log actions that are occure in a game. It mantains record of these actions in a list.
 */
public class GameActionsLogger {

    // The list represents a list of data of actions.
    private List<String> data;

    // Constructor for list of data.
    public GameActionsLogger() {
        data = new ArrayList<>();
    }

    /**
     * Logs an action with the option to print it to the console.
     * @param action The action to be logged.
     * @param print  If true, the action is also printed to the console.
     */
    public void logAction(String action, boolean print) {
        if (print) {
            System.out.println("---> " + action);
        }
        data.add(action);
    }

    /**
     * Overloaded method to log an action without printing it to the console.
     * @param action The action to be logged.
     */
    public void logAction(String action) {
        logAction(action, false);
    }

    /**
     * Creates a string representation of all logged actions.
     * @return A concatenated string of all actions, each on a new line.
     */
    @Override
    public String toString() {
        String res = "";
        boolean isFirst = true;
        for (String line : data) {
            if (isFirst) {
                isFirst = false;
            } else {
                res += "\n";
            }
            res += line;
        }
        return res;
    }
}
