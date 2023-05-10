package otava.library;

final class NoCheckResult implements Result {

    @Override
    public String asString() {
        return null;
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
