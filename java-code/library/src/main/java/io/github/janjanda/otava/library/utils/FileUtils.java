package io.github.janjanda.otava.library.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class FileUtils {
    private FileUtils() {}

    public static final Charset usedCharset = StandardCharsets.UTF_8;

    public static InputStreamReader makeFileReader(String fileName) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(fileName), usedCharset);
    }

    public static InputStreamReader makeUrlReader(String url) throws IOException {
        return new InputStreamReader(new URL(url).openStream(), usedCharset);
    }
}
