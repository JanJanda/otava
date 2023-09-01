package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.CheckFactory;

import java.net.MalformedURLException;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.utils.UrlUtils.getBaseUrlWithExc;

/**
 * This class checks base URLs of descriptors.
 */
public final class BaseUrlsCheck extends Check {
    public BaseUrlsCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ContextCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Descriptor ds : descriptors) {
            try {
                getBaseUrlWithExc(ds);
            }
            catch (MalformedURLException e) {
                resultBuilder.setFatal().addMessage(locale().cannotResolveBase(ds.getName()) + " --- " + e.getMessage());
            }
        }
        return resultBuilder.build();
    }
}
