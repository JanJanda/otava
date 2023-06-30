package otava.library.checks;

import static otava.library.utils.UrlUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import otava.library.*;
import otava.library.documents.*;
import otava.library.exceptions.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class checks whether every table has a description in descriptors and every description has a corresponding table.
 * It also checks whether table URLs in the descriptors can be resolved.
 */
public final class TableDescriptorCheck extends Check {
    public TableDescriptorCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(LineBreaksCheck.class), f.getInstance(ContextCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder();
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        List<String> extractedUrls = extractAndResolveTableUrls(resultBuilder);
        for (Table table : tables) {
            String tableName = table.getPreferredName();
            if (!extractedUrls.remove(tableName)) resultBuilder.setFatal().addMessage(Manager.locale().missingDesc(table.getName()));
        }
        for (String url : extractedUrls) {
            resultBuilder.setFatal().addMessage(Manager.locale().missingTable(url));
        }
        return resultBuilder.build();
    }

    private List<String> extractAndResolveTableUrls(Result.Builder resultBuilder) {
        List<String> urls = new ArrayList<>();
        for (Descriptor desc : descriptors) {
            String baseUrl = getBaseUrl(desc);
            List<String> extractedUrls = extractTableUrls(desc);
            for (String url : extractedUrls) {
                try {
                    urls.add(resolveUrl(baseUrl, url));
                } catch (MalformedURLException e) {
                    resultBuilder.setFatal().addMessage(Manager.locale().malformedUrl(url, baseUrl, desc.getName()));
                }
            }
        }
        return urls;
    }

    private List<String> extractTableUrls(Descriptor desc) {
        List<String> urls = new ArrayList<>();
        JsonNode urlNode = desc.path("url");
        if (urlNode.isTextual()) urls.add(urlNode.asText());
        JsonNode tablesArray = desc.path("tables");
        if (urlNode.isMissingNode() && tablesArray.isArray()) {
            for (int i = 0; i < tablesArray.size(); i++) {
                JsonNode arrayUrlNode = tablesArray.path(i).path("url");
                if (arrayUrlNode.isTextual()) urls.add(arrayUrlNode.asText());
            }
        }
        return urls;
    }
}
