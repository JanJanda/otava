package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.CheckFactory;

public class CheckA extends Check {
    public CheckA(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckB.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}
