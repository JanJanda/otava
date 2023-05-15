package otava.library;

import java.util.HashSet;
import java.util.Set;

abstract class Check {
    protected Check[] preChecks = new Check[0];
    protected Tables tables;
    protected Descriptor descriptor;
    protected Result result;

    public final void setDocuments(Tables tables, Descriptor descriptor) {
        this.tables = tables;
        this.descriptor = descriptor;
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

    public final Result validate() {
        if (result != null) return result;
        for (Check pc : preChecks) pc.validate();
        result = performValidation();
        return result;
    }

    protected abstract Result performValidation();
}
