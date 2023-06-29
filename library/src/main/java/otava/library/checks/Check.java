package otava.library.checks;

import otava.library.documents.*;
import otava.library.Result;
import otava.library.exceptions.ValidatorException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is an abstract parent for every check.
 * It handles common repetitive tasks and provides a skeleton for actual validation checks.
 * The constructor is used to configure the check.
 * A validation check needs to implement {@link #performValidation()} and return a result.
 */
public abstract class Check {
    protected final Check[] preChecks;
    protected final DocsGroup<Table> tables;
    protected final DocsGroup<Descriptor> descriptors;
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
        if (result != null) fatal = result.isFatal;
        for (Check pc : preChecks) fatal = fatal || pc.fatalSubResult();
        return fatal;
    }

    public final Result validate() throws ValidatorException {
        if (result != null) return result;
        for (Check pc : preChecks) pc.validate();
        result = performValidation();
        return result;
    }

    protected abstract Result performValidation() throws ValidatorException;
}
