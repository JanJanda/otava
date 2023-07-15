package otava.library.checks;

import otava.library.*;
import otava.library.exceptions.*;

/**
 * This class does not perform any actual check.
 * It is a root for the tree of other checks.
 */
public final class RootCheck extends Check {
    public RootCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(RequiredColumnsCheck.class), f.getInstance(PrimaryKeyCheck.class));
    }

    @Override
    protected Result performValidation() {
        return new Result.Builder().setSkipped().build();
    }
}
