package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.exceptions.ValidatorException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStreamReader;
import java.util.stream.Stream;

import static io.github.janjanda.otava.library.utils.FileUtils.makeFileReader;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class implements the validation tests from the CSVW Implementation Report.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#csvw-validation-tests-1">CSVW Implementation Report</a>
 */
@Disabled("This project does not support every specified feature because it is not required. Results: 80 failed, 201 passed")
class W3CTests {
    record TestData(String testName, Request request, String validation, String result) {}

    static Stream<TestData> dataProvider() throws Exception {
        try (InputStreamReader reader = makeFileReader("src/test/resources/w3c-tests.csv");
             CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT)) {
            Stream.Builder<TestData> data = Stream.builder();
            for (CSVRecord row : csvParser) {
                if (row.getRecordNumber() != 1) {
                    Request.Builder builder = new Request.Builder();
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
        Report report = null;
        try {
            if (data.validation().equals("full")) report = manager.fullValidation(data.request());
            if (data.validation().equals("table")) report = manager.tablesOnlyValidation(data.request());
            if (data.validation().equals("desc")) report = manager.descriptorsOnlyValidation(data.request());
        }
        catch (ValidatorException e) {
            assertEquals("fail", data.result(), data.testName());
            return;
        }

        switch (data.result()) {
            case "ok" -> assertFalse(hasFatal(report.getResults()), data.testName());
            case "fail" -> assertTrue(hasFatal(report.getResults()), data.testName());
            case "warn" -> assertFalse(allOk(report.getResults()), data.testName());
            default -> fail("Invalid test data entry!");
        }
    }

    boolean hasFatal(Result[] results) {
        for (Result result : results) {
            if (result.state == Result.State.FATAL) return true;
        }
        return false;
    }

    boolean allOk(Result[] results) {
        for (Result result : results) {
            if (result.state != Result.State.OK) return false;
        }
        return true;
    }
}
