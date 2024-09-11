package de.perkins.common.util.json;

import java.util.HashMap;
import java.util.Map;

public class JsonNode {
    private Map<String, Object> attributes = new HashMap<>();

    public JsonNode() {
    }

    public boolean isObject() {
        return true;
    }

    public void putField(String key, Object value) {
        attributes.put(key, value);
    }

    public boolean hasField(String key) {
        return attributes.containsKey(key);
    }

    public double getNumber(String key) {
        return (double) attributes.get(key);
    }

    public JsonNode getObject(String key) {
        return (JsonNode) attributes.get(key);
    }
}
