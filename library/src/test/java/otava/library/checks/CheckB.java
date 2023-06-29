package otava.library.checks;

import otava.library.*;
import otava.library.exceptions.CheckCreationException;

public class CheckB extends Check {
    public CheckB(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckC.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
