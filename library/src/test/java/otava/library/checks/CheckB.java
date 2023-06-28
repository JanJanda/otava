package otava.library.checks;

import otava.library.*;

public class CheckB extends Check {
    public CheckB(CheckFactory f) throws ValidatorException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckC.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
