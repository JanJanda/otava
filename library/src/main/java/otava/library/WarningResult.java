package otava.library;

import java.util.ArrayList;
import java.util.List;

public class WarningResult implements Result {
    private final List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public String asJson() {
        return null;
    }

    @Override
    public String asTurtle() {
        return null;
    }
}
