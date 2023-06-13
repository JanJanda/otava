package otava.library;

import otava.library.checks.Check;
import otava.library.documents.*;

public interface CheckFactory {
    Documents<Table> getTables();
    Documents<Descriptor> getDescriptors();
    <T extends Check> T getInstance(Class<T> type) throws ValidatorException;
}
