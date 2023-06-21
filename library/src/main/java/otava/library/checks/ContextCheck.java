package otava.library.checks;

import otava.library.CheckFactory;
import otava.library.documents.Descriptor;
import otava.library.results.Result;

public final class ContextCheck extends Check {
    public ContextCheck(CheckFactory f) {
        super(f.getTables(), f.getDescriptors());
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
