package otava.library;

import java.util.ArrayList;
import java.util.List;

public final class Result {
    private final List<String> messages = new ArrayList<>();
    private final boolean isFatal;

    public Result(boolean isFatal) {
        this.isFatal = isFatal;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public boolean isOk() {
        return messages.size() == 0;
    }

    public String asString() {
        return null;
    }

    public String asJson() {
        return null;
    }

    public String asTurtle() {
        return null;
    }
}
