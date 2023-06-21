package otava.library;

import java.util.ArrayList;
import java.util.List;

public final class Result {
    public final boolean isFatal;
    private final String[] messages;
    public final boolean isOk;

    private Result(Builder b) {
        this.isFatal = b.fatal;
        this.messages = b.listMessages.toArray(new String[0]);
        isOk = this.messages.length == 0;
    }

    public String asText() {
        return null;
    }

    public String asJson() {
        return null;
    }

    public String asTurtle() {
        return null;
    }

    public static final class Builder {
        private boolean fatal = false;
        private final List<String> listMessages = new ArrayList<>();

        public Builder setFatal() {
            fatal = true;
            return this;
        }

        public Builder addMessage(String message) {
            listMessages.add(message);
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }
}
