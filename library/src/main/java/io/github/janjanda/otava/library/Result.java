package io.github.janjanda.otava.library;

import java.util.ArrayList;
import java.util.List;

public final class Result {
    public final boolean isFatal;
    public final boolean isOk;
    public final boolean isSkipped;
    public final int numberOfMsg;
    public final String originCheck;
    private final String[] messages;

    private Result(Builder b) {
        isFatal = b.fatal;
        messages = b.listMessages.toArray(new String[0]);
        numberOfMsg = messages.length;
        isOk = messages.length == 0 && !isFatal;
        isSkipped = b.skipped;
        originCheck = b.origin;
    }

    public String asText() {
        StringBuilder result = new StringBuilder();
        result.append(originCheck).append("\n");
        for (String message : messages) result.append(message).append("\n");
        return result.toString();
    }

    public String asJson() {
        return null;
    }

    public String asTurtle() {
        return null;
    }

    public static final class Builder {
        private boolean fatal = false;
        private boolean skipped = false;
        private final List<String> listMessages = new ArrayList<>();
        private final String origin;

        public Builder(String originCheck) {
            origin = originCheck;
        }

        public Builder setFatal() {
            fatal = true;
            return this;
        }

        public Builder setSkipped() {
            skipped = true;
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
