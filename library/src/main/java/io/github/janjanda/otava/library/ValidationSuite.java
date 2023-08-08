package io.github.janjanda.otava.library;

import java.util.List;
import java.util.ArrayList;

public final class ValidationSuite {
    private final TableResource[] passiveTables;
    private final TableResource[] activeTables;
    private final DescResource[] passiveDescriptors;
    private final DescResource[] activeDescriptors;

    private ValidationSuite(Builder b) {
        passiveTables = b.passiveTables.toArray(new TableResource[0]);
        activeTables = b.activeTables.toArray(new TableResource[0]);
        passiveDescriptors = b.passiveDescriptors.toArray(new DescResource[0]);
        activeDescriptors = b.activeDescriptors.toArray(new DescResource[0]);
    }

    public TableResource[] getPassiveTables() {
        return passiveTables.clone();
    }

    public TableResource[] getActiveTables() {
        return activeTables.clone();
    }

    public DescResource[] getPassiveDescriptors() {
        return passiveDescriptors.clone();
    }

    public DescResource[] getActiveDescriptors() {
        return activeDescriptors.clone();
    }

    public static final class Builder {
        private final List<TableResource> passiveTables = new ArrayList<>();
        private final List<TableResource> activeTables = new ArrayList<>();
        private final List<DescResource> passiveDescriptors = new ArrayList<>();
        private final List<DescResource> activeDescriptors = new ArrayList<>();

        public Builder addPassiveTable(String name, String alias, boolean isLocal, boolean outOfMemory) {
            passiveTables.add(new TableResource(name, alias, isLocal, outOfMemory));
            return this;
        }

        public Builder addActiveTable(String name, String alias, boolean isLocal, boolean outOfMemory) {
            activeTables.add(new TableResource(name, alias, isLocal, outOfMemory));
            return this;
        }

        public Builder addPassiveDescriptor(String name, String alias, boolean isLocal) {
            passiveDescriptors.add(new DescResource(name, alias, isLocal));
            return this;
        }

        public Builder addActiveDescriptor(String name, String alias, boolean isLocal) {
            activeDescriptors.add(new DescResource(name, alias, isLocal));
            return this;
        }

        public ValidationSuite build() {
            return new ValidationSuite(this);
        }
    }

    public record TableResource(String name, String alias, boolean isLocal, boolean outOfMemory) {}

    public record DescResource(String name, String alias, boolean isLocal) {}
}
