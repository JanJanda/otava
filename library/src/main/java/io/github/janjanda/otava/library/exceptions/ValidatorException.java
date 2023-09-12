package io.github.janjanda.otava.library.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.janjanda.otava.library.Outcome;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.Manager.rdfPrefix;

public class ValidatorException extends Exception implements Outcome {
    private final String langTag = locale().langTag();
    private final String langType = locale().exception();

    public ValidatorException(String message) {
        super(message);
    }

    @Override
    public final String asText() {
        return langType + System.lineSeparator() + getMessage();
    }

    @Override
    public final String asJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode().put("type", "Exception").put("message", getMessage());
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        }
        catch (JsonProcessingException e) {
            return "Cannot write an exception as JSON.";
        }
    }

    @Override
    public final String asTurtle() {
        String label = "_:e" + this.hashCode();
        return label + " a <" + rdfPrefix + "Exception> ." + System.lineSeparator() +
               label + " <" + rdfPrefix + "message> \"" + getMessage() + "\"@" + langTag + " ." + System.lineSeparator();
    }
}
