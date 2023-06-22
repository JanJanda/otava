package otava.library.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import otava.library.*;
import otava.library.documents.*;

class ContextCheckTests {
    private ContextCheck createCheck(String descName) throws Exception {
        DocumentFactory df = new DocumentFactory();
        LocalDescriptor ld = df.getLocalDescriptor("src/test/resources/metadata/" + descName);
        DocsGroup<Descriptor> descs = new DocsGroup<>(new Descriptor[]{ld});
        SingletonCheckFactory scf = new SingletonCheckFactory(null, descs);
        return scf.getInstance(ContextCheck.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"metadata001.json", "metadata002.json", "metadata003.json", "metadata004.json"})
    void checkCorrectContexts(String fileName) throws Exception {
        assertTrue(createCheck(fileName).validate().isOk);
    }

    @ParameterizedTest
    @ValueSource(strings = {"metadata005.json", "metadata006.json", "metadata007.json",
                            "metadata008.json", "metadata009.json", "metadata010.json"})
    void checkIncorrectContexts(String fileName) throws Exception {
        assertTrue(createCheck(fileName).validate().isFatal);
    }
}
