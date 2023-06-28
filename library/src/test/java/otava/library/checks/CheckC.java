package otava.library.checks;

import otava.library.*;

public class CheckC extends Check {
    public CheckC(CheckFactory f) throws ValidatorException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckA.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
