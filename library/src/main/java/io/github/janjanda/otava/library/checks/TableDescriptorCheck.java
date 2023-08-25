package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.*;
import static io.github.janjanda.otava.library.Manager.*;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.CheckFactory;

import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;
import static io.github.janjanda.otava.library.utils.UrlUtils.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class checks whether every table has a description in descriptors and every description has a corresponding table.
 * It also checks whether table URLs in the descriptors can be resolved.
 */
public final class TableDescriptorCheck extends Check {
    public TableDescriptorCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ConsistentColumnsCheck.class), f.getInstance(IdFormatCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        List<String> extractedUrls = extractAndResolveTableUrls(resultBuilder);
        for (Table table : tables) {
            String tableName = table.getPreferredName();
            if (!extractedUrls.remove(tableName)) resultBuilder.setFatal().addMessage(locale().missingDesc(table.getName()));
        }
        for (String url : extractedUrls) {
            resultBuilder.setFatal().addMessage(locale().missingTable(url));
        }
        return resultBuilder.build();
    }

    private List<String> extractAndResolveTableUrls(Result.Builder resultBuilder) {
        List<String> urls = new ArrayList<>();
        for (Descriptor desc : descriptors) {
            String baseUrl = getBaseUrl(desc);
            List<JsonNode> tableNodes = extractTables(desc);
            for (JsonNode tableNode : tableNodes) {
                JsonNode urlNode = tableNode.path("url");
                if (urlNode.isTextual()) {
                    try {
                        urls.add(resolveUrl(baseUrl, urlNode.asText()));
                    } catch (MalformedURLException e) {
                        resultBuilder.setFatal().addMessage(locale().malformedUrl(urlNode.asText(), baseUrl, desc.getName()));
                    }
                }
            }
        }
        return urls;
    }
}
