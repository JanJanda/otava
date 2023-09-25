package io.github.janjanda.otava.web_worker;

import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Outcome;
import io.github.janjanda.otava.library.ValidationSuite;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import io.github.janjanda.otava.library.locales.CzechLocale;
import io.github.janjanda.otava.library.locales.EnglishLocale;
import io.github.janjanda.otava.library.locales.Locale;

import java.sql.*;
import java.time.Instant;
import java.util.function.BiConsumer;

public final class WebWorker {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting");
        Thread.sleep(30000);
        System.out.println("Started");
        while (true) {
            try {
                RequestData requestData = findNewJob();
                if (requestData != null) {
                    ValidationSuite.Builder vsBuilder = new ValidationSuite.Builder();
                    vsBuilder.setSaveMemory();
                    parseDocuments(requestData.passiveTables(), (a, b) -> vsBuilder.addPassiveTable(a, b, false, true));
                    parseDocuments(requestData.activeTables(), (a, b) -> vsBuilder.addActiveTable(a, b, false, true));
                    parseDocuments(requestData.passiveDescriptors(), (a, b) -> vsBuilder.addPassiveDescriptor(a, b, false));
                    parseDocuments(requestData.activeDescriptors(), (a, b) -> vsBuilder.addActiveDescriptor(a, b, false));
                    ValidationSuite vs = vsBuilder.build();

                    Manager.setLocale(makeLocale(requestData.language()));
                    Manager m = new Manager();
                    Outcome outcome = null;
                    try {
                        if (requestData.style().equals("full")) outcome = m.fullValidation(vs);
                        if (requestData.style().equals("tables")) outcome = m.tablesOnlyValidation(vs);
                        if (requestData.style().equals("descs")) outcome = m.descriptorsOnlyValidation(vs);
                    } catch (ValidatorException e) {
                        outcome = e;
                    }

                    String outcomeText = "";
                    String outcomeJson = "";
                    String outcomeTurtle = "";
                    if (outcome != null) {
                        outcomeText = outcome.asText();
                        outcomeJson = outcome.asJson();
                        outcomeTurtle = outcome.asTurtle();
                    }
                    saveOutcomes(requestData.id(), outcomeText, outcomeJson, outcomeTurtle);
                }
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            Thread.sleep(3000);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/otava", "root", System.getenv("OTAVA_DB_PSW"));
    }

    private static RequestData findNewJob() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             PreparedStatement updateRow = conn.prepareStatement("UPDATE `validations` SET `state` = 'working' WHERE `id` = ? AND `state` = 'queueing';");
             PreparedStatement selectData = conn.prepareStatement("SELECT * FROM `validations` WHERE `id` = ?;")) {

            ResultSet minId = stmt.executeQuery("SELECT MIN(`id`) FROM `validations` WHERE `state` = 'queueing';");
            minId.next();
            String rowId = minId.getString(1);
            if (rowId == null) return null;

            updateRow.setString(1, rowId);
            int changedRows = updateRow.executeUpdate();
            if (changedRows != 1) return null;

            selectData.setString(1, rowId);
            ResultSet rs = selectData.executeQuery();
            boolean found = rs.next();
            if (!found) return null;
            return new RequestData(rs.getString("id"), rs.getString("language"), rs.getString("style"),
                    rs.getString("passive-tables"), rs.getString("active-tables"), rs.getString("passive-descriptors"), rs.getString("active-descriptors"));
        }
    }

    private static void parseDocuments(String docs, BiConsumer<String, String> acceptor) {
        String[] lines = docs.trim().split("\n");
        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length == 1 && !tokens[0].equals("")) acceptor.accept(tokens[0], null);
            if (tokens.length == 2) acceptor.accept(tokens[0], tokens[1]);
        }
    }

    private static Locale makeLocale(String langTag) {
        if (langTag.equals("en")) return new EnglishLocale();
        if (langTag.equals("cs")) return new CzechLocale();
        return new EnglishLocale();
    }

    private static void saveOutcomes(String rowId, String outcomeText, String outcomeJson, String outcomeTurtle) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement updateRow = conn.prepareStatement("UPDATE `validations` SET `finish-time` = ?, `state` = ?, `outcome-text` = ?, `outcome-json` = ?, `outcome-turtle` = ? WHERE `id` = ?;")) {
            updateRow.setTimestamp(1, Timestamp.from(Instant.now()));
            updateRow.setString(2, "finished");
            updateRow.setString(3, outcomeText);
            updateRow.setString(4, outcomeJson);
            updateRow.setString(5, outcomeTurtle);
            updateRow.setString(6, rowId);
            updateRow.executeUpdate();
        }
    }

    private record RequestData(String id, String language, String style, String passiveTables, String activeTables, String passiveDescriptors, String activeDescriptors) {}
}
