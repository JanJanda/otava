package io.github.janjanda.otava.library;

import static io.github.janjanda.otava.library.utils.FileUtils.makeFileReader;
import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * This class implements the tests from the CSVW Implementation Report.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#csvw-validation-tests-1">CSVW Implementation Report</a>
 */
@Disabled("This project does not support every specified feature.")
class W3Tests {
    record TestData(String testName, ValidationSuite suite, String validation, String result) {}

    static Stream<TestData> dataProvider() throws Exception {
        try (InputStreamReader reader = makeFileReader("src/test/resources/w3tests.csv");
             CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT)) {
            Stream.Builder<TestData> data = Stream.builder();
            for (CSVRecord row : csvParser) {
                if (row.getRecordNumber() != 1) {
                    ValidationSuite.Builder builder = new ValidationSuite.Builder();
                    if (!row.get(1).isEmpty()) builder.addPassiveTable(row.get(1), null, false, false);
                    if (!row.get(2).isEmpty()) builder.addActiveTable(row.get(2), null, false, false);
                    if (!row.get(3).isEmpty()) builder.addPassiveDescriptor(row.get(3), null, false);
                    if (!row.get(4).isEmpty()) builder.addActiveDescriptor(row.get(4), null, false);
                    data.add(new TestData(row.get(0), builder.build(), row.get(5), row.get(6)));
                }
            }
            return data.build();
        }
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    void testing(TestData data) {
        Manager manager = new Manager();
        Result[] results = null;
        try {
            if (data.validation.equals("full")) results = manager.fullValidation(data.suite);
            if (data.validation.equals("table")) results = manager.tablesOnlyValidation(data.suite);
            if (data.validation.equals("desc")) results = manager.descriptorsOnlyValidation(data.suite);
        }
        catch (ValidatorException e) {
            assertEquals("fatal", data.result, data.testName);
            return;
        }

        switch (data.result) {
            case "ok" -> assertTrue(allNotFatal(results), data.testName);
            case "fail" -> assertFalse(allNotFatal(results), data.testName);
            case "warn" -> assertFalse(allOk(results), data.testName);
            default -> fail("Invalid test data entry!");
        }
    }

    boolean allNotFatal(Result[] results) {
        for (Result result : results) {
            if (result.isFatal) return false;
        }
        return true;
    }

    boolean allOk(Result[] results) {
        for (Result result : results) {
            if (!result.isOk) return false;
        }
        return true;
    }
}
