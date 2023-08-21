package io.github.janjanda.otava.library.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.janjanda.otava.library.Outcome;
import static io.github.janjanda.otava.library.Manager.*;

public class ValidatorException extends Exception implements Outcome {
    public ValidatorException(String message) {
        super(message);
    }

    @Override
    public String asText() {
        return getMessage();
    }

    @Override
    public String asJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode().put("message", getMessage());
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException e) {
            return "Cannot write an exception as JSON.";
        }
    }

    @Override
    public String asTurtle() {
        return "_:e" + random.nextInt(10000) + " <" + rdfPrefix + "message> \"" + getMessage() + "\"@" + locale().langTag() + " ." + System.lineSeparator();
    }
}
