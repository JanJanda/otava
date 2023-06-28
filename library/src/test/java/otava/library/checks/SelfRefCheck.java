package otava.library.checks;

import otava.library.*;

public class SelfRefCheck extends Check {
    public SelfRefCheck(CheckFactory f) throws ValidatorException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(SelfRefCheck.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
