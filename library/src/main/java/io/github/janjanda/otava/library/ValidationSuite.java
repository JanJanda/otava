package io.github.janjanda.otava.library;

import java.util.List;
import java.util.ArrayList;

public final class ValidationSuite {
    private final Resource[] passiveTables;
    private final Resource[] activeTables;
    private final Resource[] passiveDescriptors;
    private final Resource[] activeDescriptors;

    private ValidationSuite(Builder b) {
        passiveTables = b.passiveTables.toArray(new Resource[0]);
        activeTables = b.activeTables.toArray(new Resource[0]);
        passiveDescriptors = b.passiveDescriptors.toArray(new Resource[0]);
        activeDescriptors = b.activeDescriptors.toArray(new Resource[0]);
    }

    public int getPassiveTablesLength() {
        return passiveTables.length;
    }

    public Resource getPassiveTable(int index) {
        return passiveTables[index];
    }

    public int getActiveTablesLength() {
        return activeTables.length;
    }

    public Resource getActiveTable(int index) {
        return activeTables[index];
    }

    public int getPassiveDescriptorsLength() {
        return passiveDescriptors.length;
    }

    public Resource getPassiveDescriptor(int index) {
        return passiveDescriptors[index];
    }

    public int getActiveDescriptorsLength() {
        return activeDescriptors.length;
    }

    public Resource getActiveDescriptor(int index) {
        return activeDescriptors[index];
    }

    public static final class Builder {
        private final List<Resource> passiveTables = new ArrayList<>();
        private final List<Resource> activeTables = new ArrayList<>();
        private final List<Resource> passiveDescriptors = new ArrayList<>();
        private final List<Resource> activeDescriptors = new ArrayList<>();

        public Builder addPassiveTable(String name, String alias, boolean isLocal, boolean outOfMemory) {
            passiveTables.add(new Resource(name, alias, isLocal, outOfMemory));
            return this;
        }

        public Builder addActiveTable(String name, String alias, boolean isLocal, boolean outOfMemory) {
            activeTables.add(new Resource(name, alias, isLocal, outOfMemory));
            return this;
        }

        public Builder addPassiveDescriptor(String name, String alias, boolean isLocal, boolean outOfMemory) {
            passiveDescriptors.add(new Resource(name, alias, isLocal, outOfMemory));
            return this;
        }

        public Builder addActiveDescriptor(String name, String alias, boolean isLocal, boolean outOfMemory) {
            activeDescriptors.add(new Resource(name, alias, isLocal, outOfMemory));
            return this;
        }

        public ValidationSuite build() {
            return new ValidationSuite(this);
        }
    }

    public static final class Resource {
        public final String name;
        public final String alias;
        public final boolean isLocal;
        public final boolean outOfMemory;

        public Resource(String name, String alias, boolean isLocal, boolean outOfMemory) {
            this.name = name;
            this.alias = alias;
            this.isLocal = isLocal;
            this.outOfMemory = outOfMemory;
        }
    }
}
