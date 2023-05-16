package otava.library.checks;

import otava.library.*;

final class LineEndsCheck extends Check {
    public LineEndsCheck(CheckFactory f) {
        super(f.getTables(), f.getDescriptor());
    }

    @Override
    protected Result performValidation() {
        return new NoCheckResult();
    }
}
