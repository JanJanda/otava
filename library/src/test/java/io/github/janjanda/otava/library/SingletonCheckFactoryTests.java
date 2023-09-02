package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.checks.*;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class SingletonCheckFactoryTests {
    @Test
    void onlyOneInstanceOfEachTypeIsCreated() throws Exception {
        SingletonCheckFactory scf = new SingletonCheckFactory(null, null);
        LineBreaksCheck lbc1 = scf.getInstance(LineBreaksCheck.class);
        LineBreaksCheck lbc2 = scf.getInstance(LineBreaksCheck.class);
        assertSame(lbc1, lbc2);
    }

    @ParameterizedTest
    @ValueSource(classes = {CheckA.class, CheckB.class, CheckC.class, SelfRefCheck.class})
    void cyclicalPreChecksThrow(Class<? extends Check> check) {
        SingletonCheckFactory scf = new SingletonCheckFactory(null, null);
        assertThrowsExactly(CheckCreationException.class, () -> scf.getInstance(check));
    }
}
