package io.github.janjanda.otava.library.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.documents.Descriptor;

public record DescAndTable(Descriptor descriptor, JsonNode tableNode) {}
