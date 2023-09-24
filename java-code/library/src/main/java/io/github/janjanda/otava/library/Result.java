package io.github.janjanda.otava.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.Manager.rdfPrefix;
import static io.github.janjanda.otava.library.utils.StringUtils.escapeTurtleString;

/**
 * This class represent a result of a validation check.
 */
public final class Result {
    /**
     * check which created this result
     */
    public final String originCheck;
    /**
     * duration of the execution of the check
     */
    public final Duration duration;
    /**
     * state of this result
     */
    public final State state;
    private final String[] messages;
    /**
     * number of messages
     */
    public final int numberOfMsg;

    private final String langDuration = locale().duration();
    private final String langTag = locale().langTag();
    private final String langWarn = locale().warning();
    private final String langFatal = locale().fatal();
    private final String langSkipped = locale().skipped();
    private final String langType = locale().result();
    /**
     * The subject of the Turtle representation. The subject represents this result in Turtle.
     */
    public final String turtleLabel = "_:r" + this.hashCode();

    private Result(Builder b) {
        originCheck = b.origin;
        duration = b.duration;
        state = b.state;
        messages = b.listMessages.toArray(new String[0]);
        numberOfMsg = messages.length;
    }

    /**
     * Gest a message from an array with messages.
     * @param index index of the message
     * @return message from an array with messages
     */
    public String getMessage(int index) {
        return messages[index];
    }

    /**
     * Crates a textual representation of this result.
     * @return textual representation of this result
     */
    public String toText() {
        StringBuilder result = new StringBuilder();
        result.append(langType).append(System.lineSeparator());
        result.append(originCheck).append(System.lineSeparator());
        result.append(langDuration).append(": ").append(convertDuration()).append(" s").append(System.lineSeparator());
        switch (state) {
            case OK -> result.append("ok").append(System.lineSeparator());
            case WARN -> result.append(langWarn).append(System.lineSeparator());
            case FATAL -> result.append(langFatal).append(System.lineSeparator());
            case SKIPPED -> result.append(langSkipped).append(System.lineSeparator());
        }
        for (String message : messages) result.append(message).append(System.lineSeparator());
        return result.toString();
    }

    /**
     * Creates a JSON representation of this result.
     * @return JSON representation of this result
     */
    public ObjectNode toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode()
                .put("type", "Result")
                .put("check", originCheck)
                .put("duration", convertDuration())
                .put("state", state.toString());
        ArrayNode msgs = root.putArray("messages");
        for (String msg : messages) msgs.add(msg);
        return root;
    }

    /**
     * Creates a representation of this result in RDF 1.1 Turtle.
     * @return representation of this result in RDF 1.1 Turtle
     */
    public String toTurtle() {
        StringBuilder output = new StringBuilder();
        output.append(turtleLabel).append(" a <").append(rdfPrefix).append("Result> .").append(System.lineSeparator());
        output.append(turtleLabel).append(" <").append(rdfPrefix).append("checkName> \"").append(escapeTurtleString(originCheck)).append("\" .").append(System.lineSeparator());
        output.append(turtleLabel).append(" <").append(rdfPrefix).append("duration> ").append(convertDuration()).append(" .").append(System.lineSeparator());
        output.append(turtleLabel).append(" <").append(rdfPrefix).append("state> \"").append(escapeTurtleString(state.toString())).append("\" .").append(System.lineSeparator());
        for (String msg : messages) {
            output.append(turtleLabel).append(" <").append(rdfPrefix).append("message> \"").append(escapeTurtleString(msg)).append("\"@").append(langTag).append(" .").append(System.lineSeparator());
        }
        return output.toString();
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
