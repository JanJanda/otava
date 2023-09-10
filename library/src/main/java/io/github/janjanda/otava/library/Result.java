package io.github.janjanda.otava.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.janjanda.otava.library.Manager.*;

/**
 * This class represent a result of a validation check.
 */
public final class Result implements Outcome {
    public final String originCheck;
    public final Duration duration;
    public final State state;
    private final String[] messages;
    public final int numberOfMsg;

    private final String langDuration = locale().duration();
    private final String langTag = locale().langTag();
    private final String langWarn = locale().warning();
    private final String langFatal = locale().fatal();
    private final String langSkipped = locale().skipped();

    private Result(Builder b) {
        originCheck = b.origin;
        duration = b.duration;
        state = b.state;
        messages = b.listMessages.toArray(new String[0]);
        numberOfMsg = messages.length;
    }

    public String getMessage(int index) {
        return messages[index];
    }

    @Override
    public String asText() {
        StringBuilder result = new StringBuilder();
        result.append(originCheck).append(System.lineSeparator());
        result.append(langDuration).append(": ").append(formatDuration()).append(System.lineSeparator());
        switch (state) {
            case OK -> result.append("ok").append(System.lineSeparator());
            case WARN -> result.append(langWarn).append(System.lineSeparator());
            case FATAL -> result.append(langFatal).append(System.lineSeparator());
            case SKIPPED -> result.append(langSkipped).append(System.lineSeparator());
        }
        for (String message : messages) result.append(message).append(System.lineSeparator());
        return result.toString();
    }

    @Override
    public String asJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode()
                .put("check", originCheck)
                .put("state", state.toString());
        if (duration != null) root.put("duration", convertDuration());
        ArrayNode msgs = root.putArray("messages");
        for (String msg : messages) {
            msgs.add(msg);
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        }
        catch (JsonProcessingException e) {
            return "Cannot write a result as JSON.";
        }
    }

    @Override
    public String asTurtle() {
        String label = "_:r" + random.nextInt(10000);
        StringBuilder output = new StringBuilder();
        output.append(label).append(" <").append(rdfPrefix).append("check> \"").append(originCheck).append("\" .").append(System.lineSeparator());
        if (duration != null) output.append(label).append(" <").append(rdfPrefix).append("duration> ").append(convertDuration()).append(" .").append(System.lineSeparator());
        output.append(label).append(" <").append(rdfPrefix).append("state> \"").append(state.toString()).append("\" .").append(System.lineSeparator());
        for (String msg : messages) {
            output.append(label).append(" <").append(rdfPrefix).append("message> \"").append(msg).append("\"@").append(langTag).append(" .").append(System.lineSeparator());
        }
        return output.toString();
    }

    private String formatDuration() {
        if (duration == null) return "?";
        return convertDuration() + " s";
    }

    private double convertDuration() {
        return duration.toMillis() / 1000d;
    }

    /**
     * This builder provides a convenient syntax for the input of result data.
     */
    public static final class Builder {
        private final String origin;
        private Duration duration;
        private State state = State.OK;
        private final List<String> listMessages = new ArrayList<>();

        public Builder(String originCheck) {
            origin = originCheck;
        }

        public Builder setFatal() {
            state = State.FATAL;
            return this;
        }

        public Builder setSkipped() {
            state = State.SKIPPED;
            return this;
        }

        public Builder addMessage(String message) {
            if (state == State.OK) state = State.WARN;
            listMessages.add(message);
            return this;
        }

        public Builder setDuration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    public enum State {
        OK,
        WARN,
        FATAL,
        SKIPPED
    }
}
