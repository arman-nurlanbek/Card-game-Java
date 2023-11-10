import java.util.ArrayList;
import java.util.List;

public class GameActionsLogger {

    private List<String> data;

    public GameActionsLogger() {
        data = new ArrayList<>();
    }

    public void logAction(String action) {
        data.add(action);
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
