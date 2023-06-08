package otava.library;

import java.util.ArrayList;
import java.util.List;

public final class WarningResult implements Result {
    private final List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public boolean isOk() {
        return false;
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
