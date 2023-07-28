package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;

/**
 * This class does not perform any actual check.
 * It is a root for the tree of other checks.
 */
public final class RootCheck extends Check {
    public RootCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(RequiredColumnsCheck.class), f.getInstance(DataTypesCheck.class), f.getInstance(ForeignKeyCheck.class));
    }

    @Override
    protected Result performValidation() {
        return new Result.Builder(this.getClass().getName()).build();
    }
}
