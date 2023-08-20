package io.github.janjanda.otava.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static io.github.janjanda.otava.library.Manager.*;
import java.util.ArrayList;
import java.util.List;

public final class Result implements Outcome {
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

    @Override
    public String asText() {
        StringBuilder result = new StringBuilder();
        result.append(originCheck).append("\n");
        if (isOk) result.append("ok").append("\n");
        if (isFatal) result.append(locale().fatal()).append("\n");
        if (isSkipped) result.append(locale().skipped()).append("\n");
        for (String message : messages) result.append(message).append("\n");
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
        ArrayNode msgs = root.putArray("messages");
        for (String msg : messages) {
            msgs.add(msg);
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException e) {
            return "Cannot write a result as JSON.";
        }
    }

    @Override
    public String asTurtle() {
        String label = "_:r" + random.nextInt(10000);
        StringBuilder output = new StringBuilder();
        output.append(label).append(" <").append(rdfPrefix).append("check> \"").append(originCheck).append("\" .");
        output.append(label).append(" <").append(rdfPrefix).append("fatal> ").append(isFatal).append(" .");
        output.append(label).append(" <").append(rdfPrefix).append("ok> ").append(isOk).append(" .");
        output.append(label).append(" <").append(rdfPrefix).append("skipped> ").append(isSkipped).append(" .");
        for (String msg : messages) {
            output.append(label).append(" <").append(rdfPrefix).append("message> \"").append(msg).append("\"@").append(locale().langTag()).append(" .");
        }
        return output.toString();
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
