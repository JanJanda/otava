package otava.library;

import otava.library.checks.Check;
import otava.library.documents.*;

public interface CheckFactory {
    Tables getTables();
    Descriptor getDescriptor();
    <T extends Check> T getInstance(Class<T> type) throws ValidatorException;
}
