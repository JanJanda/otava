package otava.library.checks;

import otava.library.*;
import otava.library.exceptions.CheckCreationException;

public class CheckA extends Check {
    public CheckA(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckB.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
