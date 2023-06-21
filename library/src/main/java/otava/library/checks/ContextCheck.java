package otava.library.checks;

import otava.library.CheckFactory;
import otava.library.Manager;
import otava.library.Result;
import otava.library.documents.Descriptor;

public final class ContextCheck extends Check {
    public ContextCheck(CheckFactory f) {
        super(f.getTables(), f.getDescriptors());
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder();
        for (Descriptor descriptor : descriptors) {
            if (contextWrong(descriptor)) resultBuilder.addMessage(Manager.locale().badContext(descriptor.getName())).setFatal();
        }
        return resultBuilder.build();
    }

    private boolean contextWrong(Descriptor descriptor) {
        if (descriptor.path("@context").asText().equals("http://www.w3.org/ns/csvw")) return false;
        return true; // todo: finish
    }
}
