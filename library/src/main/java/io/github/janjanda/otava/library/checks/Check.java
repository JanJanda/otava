package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.FileIteratorException;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is an abstract parent for every check.
 * It handles common repetitive tasks and provides a skeleton for actual validation checks.
 * The constructor is used to configure the check.
 * A validation check needs to implement {@link #performValidation()} and return a result builder with results.
 */
public abstract class Check {
    protected final DocsGroup<Table> tables;
    protected final DocsGroup<Descriptor> descriptors;
    private final Check[] preChecks;
    private Result result;

    protected Check(DocsGroup<Table> tables, DocsGroup<Descriptor> descriptors, Check... preChecks) {
        this.tables = tables;
        this.descriptors = descriptors;
        this.preChecks = preChecks;
    }

    public final Result getResult() {
        return result;
    }

    public final Set<Result> getAllResults() {
        Set<Result> results = new HashSet<>();
        results.add(result);
        for (Check pc : preChecks) results.addAll(pc.getAllResults());
        return results;
    }

    public final boolean fatalSubResult() {
        boolean fatal = false;
        if (result != null) fatal = result.state == Result.State.FATAL;
        for (Check pc : preChecks) fatal = fatal || pc.fatalSubResult();
        return fatal;
    }

    public final Result validate() throws ValidatorException {
        if (result != null) return result;
        for (Check pc : preChecks) pc.validate();
        try {
            Instant start = Instant.now();
            Result.Builder resultBuilder = performValidation();
            Instant end = Instant.now();
            resultBuilder.setDuration(Duration.between(start, end));
            result = resultBuilder.build();
            return result;
        }
        catch (FileIteratorException e) {
            throw new ValidatorFileException(e.getMessage());
        }
    }

    protected abstract Result.Builder performValidation() throws ValidatorException;

    public final String printTree() {
        StringBuilder result = new StringBuilder();
        printTreeRecursive(result, "", true, true);
        return result.toString();
    }

    private void printTreeRecursive(StringBuilder result, String padding, boolean isLast, boolean isRoot) {
        String turn = "";
        String nextPadding = padding;
        if (!isRoot) {
            turn = isLast ? "└──" : "├──";
            nextPadding += isLast ? "   " : "│  ";
        }
        result.append(padding).append(turn).append(this.getClass().getSimpleName()).append(System.lineSeparator());
        for (int i = 0; i < preChecks.length; i++) {
            preChecks[i].printTreeRecursive(result, nextPadding, i == preChecks.length - 1, false);
        }
    }
}
