package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.CheckFactory;

public class CheckA extends Check {
    public CheckA(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(CheckB.class));
    }

    @Override
    protected Result.Builder performValidation() {
        return new Result.Builder(this.getClass().getName());
    }
}
