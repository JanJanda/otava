package otava.library;

import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import otava.library.checks.*;

class SingletonCheckFactoryTests {
    @Test
    void onlyOneInstanceOfEachTypeIsCreated() throws Exception {
        SingletonCheckFactory scf = new SingletonCheckFactory(null, null);
        LineBreaksCheck lbc1 = scf.getInstance(LineBreaksCheck.class);
        LineBreaksCheck lbc2 = scf.getInstance(LineBreaksCheck.class);
        assertSame(lbc1, lbc2);
    }
}
