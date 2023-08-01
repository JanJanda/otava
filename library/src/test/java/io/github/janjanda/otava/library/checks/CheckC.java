package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;

public class CheckC extends Check {
    public CheckC(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckA.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
