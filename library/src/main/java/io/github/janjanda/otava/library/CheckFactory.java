package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.checks.Check;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;

public interface CheckFactory {
    DocsGroup<Table> getTables();
    DocsGroup<Descriptor> getDescriptors();
    <T extends Check> T getInstance(Class<T> type) throws CheckCreationException;
}
