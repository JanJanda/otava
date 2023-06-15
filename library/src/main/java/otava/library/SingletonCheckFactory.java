package otava.library;

import otava.library.checks.Check;
import otava.library.documents.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Constructor;

public final class SingletonCheckFactory implements CheckFactory {
    private final Map<Class<?>, Check> instances = new HashMap<>();
    private final Set<Class<?>> unfinishedInstances = new HashSet<>();
    private final Documents<Table> tables;
    private final Documents<Descriptor> descriptors;

    public SingletonCheckFactory(Documents<Table> tables, Documents<Descriptor> descriptors) {
        this.tables = tables;
        this.descriptors = descriptors;
    }

    @Override
    public Documents<Table> getTables() {
        return tables;
    }

    @Override
    public Documents<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Check> T getInstance(Class<T> type) throws ValidatorException {
        if (instances.containsKey(type)) return (T)instances.get(type);
        try {
            Constructor<T> ctor = type.getConstructor(CheckFactory.class);
            if (unfinishedInstances.contains(type)) throw new ValidatorException(Manager.locale().cyclicalDeps());
            unfinishedInstances.add(type);
            T check = ctor.newInstance(this);
            unfinishedInstances.remove(type);
            instances.put(type, check);
            return check;
        }
        catch (ReflectiveOperationException e) {
            throw new ValidatorException(Manager.locale().badCtor(type.getName()));
        }
    }
}
