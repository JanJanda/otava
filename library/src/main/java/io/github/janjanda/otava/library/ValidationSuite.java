package io.github.janjanda.otava.library;

import java.util.ArrayList;
import java.util.List;

/**
 * This class only collects input data for validation. It does not do anything with the data. It has a convenient builder.
 */
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

    /**
     * Gets a clone of the array with passive tables.
     * @return clone of the array with passive tables
     */
    public TableResource[] getPassiveTables() {
        return passiveTables.clone();
    }

    /**
     * Gets a clone of the array with active tables.
     * @return clone of the array with active tables
     */
    public TableResource[] getActiveTables() {
        return activeTables.clone();
    }

    /**
     * Gets a clone of the array with passive descriptors.
     * @return clone of the array with passive descriptors
     */
    public DescResource[] getPassiveDescriptors() {
        return passiveDescriptors.clone();
    }

    /**
     * Gets a clone of the array with active descriptors.
     * @return clone of the array with active descriptors
     */
    public DescResource[] getActiveDescriptors() {
        return activeDescriptors.clone();
    }

    /**
     * This builder provides a convenient syntax for the input of validation data. It only collects the data.
     */
    public static final class Builder {
        private final List<TableResource> passiveTables = new ArrayList<>();
        private final List<TableResource> activeTables = new ArrayList<>();
        private final List<DescResource> passiveDescriptors = new ArrayList<>();
        private final List<DescResource> activeDescriptors = new ArrayList<>();

        /**
         * Adds a passive table to the builder. Passive tables do not attempt to locate their descriptors.
         * @param name name of the file with the table, path for local file, URL for online file
         * @param alias alternative name for validation purposes
         * @param isLocal {@code true} if the file is local in filesystem, {@code false} if the file is online
         * @param outOfMemory for large files, file will not be loaded to memory
         * @return reference to this builder
         */
        public Builder addPassiveTable(String name, String alias, boolean isLocal, boolean outOfMemory) {
            passiveTables.add(new TableResource(name, alias, isLocal, outOfMemory));
            return this;
        }

        /**
         * Adds an active table to the builder. Active tables attempt to locate their descriptors before validation.
         * @param name name of the file with the table, path for local file, URL for online file
         * @param alias alternative name for validation purposes
         * @param isLocal {@code true} if the file is local in filesystem, {@code false} if the file is online
         * @param outOfMemory for large files, file will not be loaded to memory
         * @return reference to this builder
         */
        public Builder addActiveTable(String name, String alias, boolean isLocal, boolean outOfMemory) {
            activeTables.add(new TableResource(name, alias, isLocal, outOfMemory));
            return this;
        }

        /**
         * Adds a passive descriptor to the builder. Passive descriptors do not attempt to locate described tables.
         * @param name name of the file with the descriptor, path for local file, URL for online file
         * @param alias alternative name for validation purposes
         * @param isLocal {@code true} if the file is local in filesystem, {@code false} if the file is online
         * @return reference to this builder
         */
        public Builder addPassiveDescriptor(String name, String alias, boolean isLocal) {
            passiveDescriptors.add(new DescResource(name, alias, isLocal));
            return this;
        }

        /**
         * Adds an active descriptor to the builder. Active descriptors attempt to locate described tables before validation.
         * @param name name of the file with the descriptor, path for local file, URL for online file
         * @param alias alternative name for validation purposes
         * @param isLocal {@code true} if the file is local in filesystem, {@code false} if the file is online
         * @return reference to this builder
         */
        public Builder addActiveDescriptor(String name, String alias, boolean isLocal) {
            activeDescriptors.add(new DescResource(name, alias, isLocal));
            return this;
        }

        /**
         * Creates a new validation suite with the data from this builder.
         * @return new validation suite with the data from this builder
         */
        public ValidationSuite build() {
            return new ValidationSuite(this);
        }
    }

    /**
     * Simple record for the data about a table.
     */
    public record TableResource(String name, String alias, boolean isLocal, boolean outOfMemory) {}

    /**
     * Simple record for the data about a descriptor.
     */
    public record DescResource(String name, String alias, boolean isLocal) {}
}
