package otava.library.results;

public interface Result {
    boolean isOk();
    String asString();
    String asJson();
    String asTurtle();
}
