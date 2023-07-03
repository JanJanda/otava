package otava.library.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class FileUtils {
    private FileUtils() {}

    public static InputStreamReader makeReader(String fileName) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
    }
}
