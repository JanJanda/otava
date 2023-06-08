package otava.library;

public final class OkResult implements Result {

    @Override
    public boolean isOk() {
        return true;
    }

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
