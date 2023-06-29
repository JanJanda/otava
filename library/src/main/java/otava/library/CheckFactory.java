package otava.library;

import otava.library.checks.Check;
import otava.library.documents.*;
import otava.library.exceptions.CheckCreationException;

public interface CheckFactory {
    DocsGroup<Table> getTables();
    DocsGroup<Descriptor> getDescriptors();
    <T extends Check> T getInstance(Class<T> type) throws CheckCreationException;
}
