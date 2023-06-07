package otava.library;

import otava.library.checks.Check;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Constructor;

public final class SingletonCheckFactory implements CheckFactory {
    private final Map<Class<?>, Check> instances = new HashMap<>();
    private final Set<Class<?>> unfinishedInstances = new HashSet<>();
    private final Tables tables;
    private final Descriptor descriptor;

    public SingletonCheckFactory(Tables tables, Descriptor descriptor) {
        this.tables = tables;
        this.descriptor = descriptor;
    }

    @Override
    public Tables getTables() {
        return tables;
    }

    @Override
    public Descriptor getDescriptor() {
        return descriptor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Check> T getInstance(Class<T> type) throws ValidatorException {
        if (instances.containsKey(type)) return (T)instances.get(type);
        try {
            Constructor<T> ctor = type.getConstructor(CheckFactory.class);
            if (unfinishedInstances.contains(type)) throw new ValidatorException("Pre-check dependencies are cyclical.");
            unfinishedInstances.add(type);
            T check = ctor.newInstance(this);
            unfinishedInstances.remove(type);
            instances.put(type, check);
            return check;
        }
        catch (ReflectiveOperationException e) {
            throw new ValidatorException("The check " + type.getName() + " does not have the expected constructor.");
        }
    }
}
