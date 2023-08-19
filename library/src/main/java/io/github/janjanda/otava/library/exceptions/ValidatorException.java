package io.github.janjanda.otava.library.exceptions;

import io.github.janjanda.otava.library.Outcome;

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
        return null;
    }

    @Override
    public String asTurtle() {
        return null;
    }
}
