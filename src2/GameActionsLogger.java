import java.util.ArrayList;
import java.util.List;

public class GameActionsLogger {

    private List<String> data;

    public GameActionsLogger() {
        data = new ArrayList<>();
    }

    public void logAction(String action, boolean print) {
        if (print) {
            System.out.println("---> " + action);
        }
        data.add(action);
    }

    public void logAction(String action) {
        logAction(action, false);
    }

    @Override
    public String toString() {
        String res = "";
        for (String line : data) {
            res += line + "\n";
        }
        return res;
    }
}
