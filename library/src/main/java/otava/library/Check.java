package otava.library;

import java.util.HashSet;
import java.util.Set;

abstract class Check {
    Check[] preChecks = new Check[0];
    Table[] tables = new Table[0];
    Descriptor descriptor;
    Result result;

    public Check(Table[] tables) {
        this.tables = tables;
    }

    public Check(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public Check(Table[] tables, Descriptor descriptor) {
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

    Result performValidation() {
        return new NoCheckResult();
    }
}
