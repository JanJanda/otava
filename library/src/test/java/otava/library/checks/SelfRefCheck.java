package otava.library.checks;

import otava.library.*;
import otava.library.exceptions.CheckCreationException;

public class SelfRefCheck extends Check {
    public SelfRefCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(SelfRefCheck.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
