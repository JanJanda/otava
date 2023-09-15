package io.github.janjanda.otava.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Duration;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.Manager.rdfPrefix;
import static io.github.janjanda.otava.library.utils.StringUtils.escapeTurtleString;

/**
 * This class contains results and other data about performed validation.
 */
public final class Report implements Outcome {
    /**
     * total measured duration of the validation
     */
    public final Duration totalDuration;
    private final String[] tableNames;
    private final String[] descNames;
    private final Result[] results;

    private final String langReport = locale().validationReport();
    private final String langTotalDuration = locale().totalDuration();
    private final String langTables = locale().tables();
    private final String langDescs = locale().descriptors();
    private final String langResults = locale().results();

    public Report(Duration totalDuration, String[] tableNames, String[] descNames, Result[] results) {
        this.totalDuration = totalDuration;
        this.tableNames = tableNames.clone();
        this.descNames = descNames.clone();
        this.results = results.clone();
    }

    /**
     * Gets used names of the validated tables.
     * @return used names of the validated tables
     */
    public String[] getTableNames() {
        return tableNames.clone();
    }

    /**
     * Gets used names of the validated descriptors.
     * @return used names of the validated descriptors
     */
    public String[] getDescNames() {
        return descNames.clone();
    }

    /**
     * Gets results of the validation checks.
     * @return results of the validation checks
     */
    public Result[] getResults() {
        return results.clone();
    }

    @Override
    public String asText() {
        StringBuilder sb = new StringBuilder();
        sb.append(langReport).append(System.lineSeparator());
        sb.append(langTotalDuration).append(": ").append(convertDuration()).append(" s").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(langTables).append(":").append(System.lineSeparator());
        for (String tableName : tableNames) sb.append(tableName).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(langDescs).append(":").append(System.lineSeparator());
        for (String descName : descNames) sb.append(descName).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(langResults).append(":").append(System.lineSeparator());
        for (Result result : results) sb.append(result.toText()).append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String asJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        root.put("type", "Report");
        root.put("totalDuration", convertDuration());
        ArrayNode tablesArray = root.putArray("tables");
        for (String tableName : tableNames) tablesArray.add(tableName);
        ArrayNode descsArray = root.putArray("descriptors");
        for (String descName : descNames) descsArray.add(descName);
        ArrayNode resultsArray = root.putArray("results");
        for (Result result : results) resultsArray.add(result.toJson());
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root) + System.lineSeparator();
        }
        catch (JsonProcessingException e) {
            return "Cannot write report as JSON.";
        }
    }

    @Override
    public String asTurtle() {
        StringBuilder sb = new StringBuilder();
        String label = "_:rep" + this.hashCode();
        sb.append(label).append(" a <").append(rdfPrefix).append("Report> .").append(System.lineSeparator());
        sb.append(label).append(" <").append(rdfPrefix).append("totalDuration> ").append(convertDuration()).append(" .").append(System.lineSeparator());
        for (String tableName : tableNames) {
            sb.append(label).append(" <").append(rdfPrefix).append("tableName> \"").append(escapeTurtleString(tableName)).append("\" .").append(System.lineSeparator());
        }
        for (String descName : descNames) {
            sb.append(label).append(" <").append(rdfPrefix).append("descriptorName> \"").append(escapeTurtleString(descName)).append("\" .").append(System.lineSeparator());
        }
        for (Result result : results) {
            sb.append(label).append(" <").append(rdfPrefix).append("result> ").append(result.turtleLabel).append(" .").append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        for (Result result : results) {
            sb.append(result.toTurtle()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private double convertDuration() {
        return totalDuration.toMillis() / 1000d;
    }
}
