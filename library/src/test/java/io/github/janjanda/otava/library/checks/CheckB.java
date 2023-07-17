package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;

public class CheckB extends Check {
    public CheckB(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckC.class));
    }

    @Override
    protected Result performValidation() {
        return null;
    }
}