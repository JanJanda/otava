package io.github.janjanda.otava.library;

import static io.github.janjanda.otava.library.Manager.*;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.checks.Check;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Constructor;

public final class SingletonCheckFactory implements CheckFactory {
    private final Map<Class<?>, Check> instances = new HashMap<>();
    private final Set<Class<?>> unfinishedInstances = new HashSet<>();
    private final DocsGroup<Table> tables;
    private final DocsGroup<Descriptor> descriptors;

    public SingletonCheckFactory(DocsGroup<Table> tables, DocsGroup<Descriptor> descriptors) {
        this.tables = tables;
        this.descriptors = descriptors;
    }

    @Override
    public DocsGroup<Table> getTables() {
        return tables;
    }

    @Override
    public DocsGroup<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Check> T getInstance(Class<T> type) throws CheckCreationException {
        if (instances.containsKey(type)) return (T)instances.get(type);
        try {
            Constructor<T> ctor = type.getConstructor(CheckFactory.class);
            if (unfinishedInstances.contains(type)) throw new ReflectiveOperationException(); // This line detects cyclical pre-check dependencies in the check instances.
            unfinishedInstances.add(type);
            T check = ctor.newInstance(this);
            unfinishedInstances.remove(type);
            instances.put(type, check);
            return check;
        }
        catch (ReflectiveOperationException e) {
            throw new CheckCreationException(locale().checkCreationFail(type.getName()));
        }
    }
}
