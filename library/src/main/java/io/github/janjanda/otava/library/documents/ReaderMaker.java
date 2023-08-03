package io.github.janjanda.otava.library.documents;

import java.io.IOException;
import java.io.InputStreamReader;

@FunctionalInterface
public interface ReaderMaker {
    InputStreamReader makeReader(String fileName) throws IOException;
}
