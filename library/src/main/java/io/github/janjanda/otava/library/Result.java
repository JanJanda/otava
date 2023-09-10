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
    public final Duration duration;
    public final boolean isFatal;
    public final boolean isOk;
    public final boolean isSkipped;
    public final String originCheck;
    private final String[] messages;
    public final int numberOfMsg;
    private final String langDuration = locale().duration();
    private final String langTag = locale().langTag();
    private final String langFatal = locale().fatal();
    private final String langSkipped = locale().skipped();
    private final String langDecimal = locale().decimal();

    private Result(Builder b) {
        duration = b.duration;
        isFatal = b.fatal;
        messages = b.listMessages.toArray(new String[0]);
        numberOfMsg = messages.length;
        isSkipped = b.skipped;
        originCheck = b.origin;
        isOk = messages.length == 0 && !isFatal && !isSkipped;
    }

    @Override
    public String asText() {
        StringBuilder result = new StringBuilder();
        result.append(originCheck).append(System.lineSeparator());
        result.append(langDuration).append(": ").append(formatDuration()).append(System.lineSeparator());
        if (isOk) result.append("ok").append(System.lineSeparator());
        if (isFatal) result.append(langFatal).append(System.lineSeparator());
        if (isSkipped) result.append(langSkipped).append(System.lineSeparator());
        for (String message : messages) result.append(message).append(System.lineSeparator());
        return result.toString();
    }

    @Override
    public String asJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode()
                .put("check", originCheck)
                .put("fatal", isFatal)
                .put("ok", isOk)
                .put("skipped", isSkipped);
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
        output.append(label).append(" <").append(rdfPrefix).append("fatal> ").append(isFatal).append(" .").append(System.lineSeparator());
        output.append(label).append(" <").append(rdfPrefix).append("ok> ").append(isOk).append(" .").append(System.lineSeparator());
        output.append(label).append(" <").append(rdfPrefix).append("skipped> ").append(isSkipped).append(" .").append(System.lineSeparator());
        for (String msg : messages) {
            output.append(label).append(" <").append(rdfPrefix).append("message> \"").append(msg).append("\"@").append(langTag).append(" .").append(System.lineSeparator());
        }
        return output.toString();
    }

    private String formatDuration() {
        if (duration == null) return "?";
        long seconds = duration.getSeconds();
        int millis = duration.getNano() / 1_000_000;
        return seconds + langDecimal + millis + " s";
    }

    private double convertDuration() {
        return duration.toMillis() / 1000d;
    }

    /**
     * This builder provides a convenient syntax for the input of result data.
     */
    public static final class Builder {
        private Duration duration;
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

        public Builder setDuration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }
}
