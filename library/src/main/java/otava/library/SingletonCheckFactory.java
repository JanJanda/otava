package otava.library;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Constructor;

class SingletonCheckFactory implements CheckFactory {
    private final Map<Class<?>, Check> instances = new HashMap<>();
    private final Tables tables;
    private final Descriptor descriptor;

    public SingletonCheckFactory(Tables tables, Descriptor descriptor) {
        this.tables = tables;
        this.descriptor = descriptor;
    }

    @Override
    public<T extends Check> Check getInstance(Class<T> type) throws ValidatorException {
        if (instances.containsKey(type)) return instances.get(type);
        try {
            Constructor<T> ctor = type.getConstructor(CheckFactory.class);
            T check = ctor.newInstance(this);
            check.setDocuments(tables, descriptor);
            instances.put(type, check);
            return check;
        }
        catch (Exception e) {
            throw new ValidatorException("The check does not have the expected constructor.");
        }
    }
}
