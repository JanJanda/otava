package otava.library;

public interface Result {
    boolean isOk();
    String asString();
    String asJson();
    String asTurtle();
}
