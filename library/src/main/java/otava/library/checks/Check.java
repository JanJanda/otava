package otava.library.checks;

import otava.library.*;
import otava.library.documents.*;
import otava.library.results.Result;
import java.util.HashSet;
import java.util.Set;

public abstract class Check {
    private final Check[] preChecks;
    protected final Tables tables;
    protected final Descriptor descriptor;
    private Result result;

    protected Check(Tables tables, Descriptor descriptor, Check... preChecks) {
        this.tables = tables;
        this.descriptor = descriptor;
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

    public final Result validate() throws ValidatorException {
        if (result != null) return result;
        for (Check pc : preChecks) pc.validate();
        result = performValidation();
        return result;
    }

    protected abstract Result performValidation() throws ValidatorException;
}
