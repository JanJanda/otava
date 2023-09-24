package io.github.janjanda.otava.library;

public interface Outcome {

    /**
     * Creates a textual representation of this outcome.
     * @return textual representation of this outcome
     */
    String asText();

    /**
     * Creates a JSON representation of this outcome.
     * @return JSON representation of this outcome
     */
    String asJson();

    /**
     * Creates a representation of this outcome in RDF 1.1 Turtle.
     * @return representation of this outcome in RDF 1.1 Turtle
     */
    String asTurtle();
}
