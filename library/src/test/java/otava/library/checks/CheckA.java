package otava.library.checks;

import otava.library.*;

public class CheckA extends Check {
    public CheckA(CheckFactory f) throws ValidatorException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckB.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
