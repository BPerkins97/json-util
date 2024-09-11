package de.perkins.common.util.json;

public class Json {
    public static JsonNode parse(String json) {
        return new JsonParser(json).parse();
    }
}
