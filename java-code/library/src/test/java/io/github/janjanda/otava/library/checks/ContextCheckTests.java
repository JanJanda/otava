package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.TestUtils;
import io.github.janjanda.otava.library.documents.BasicDescriptor;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContextCheckTests {
    private ContextCheck createCheck(String descName) throws Exception {
        BasicDescriptor bd = TestUtils.createDescriptor(descName, null);
        DocsGroup<Descriptor> descs = new DocsGroup<>(new Descriptor[]{bd});
        SingletonCheckFactory scf = new SingletonCheckFactory(null, descs);
        return scf.getInstance(ContextCheck.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"metadata001.json", "metadata002.json", "metadata003.json", "metadata004.json"})
    void checkCorrectContexts(String fileName) throws Exception {
        assertEquals(Result.State.OK, createCheck(fileName).validate().state);
    }

    @ParameterizedTest
    @ValueSource(strings = {"metadata005.json", "metadata006.json", "metadata007.json",
                            "metadata008.json", "metadata009.json", "metadata010.json"})
    void checkIncorrectContexts(String fileName) throws Exception {
        assertEquals(Result.State.FATAL, createCheck(fileName).validate().state);
    }
}
