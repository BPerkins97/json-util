package de.perkins.common.util.json;

import java.util.HashMap;
import java.util.Map;

public class JsonNode {
    private Map<String, Integer> attributes = new HashMap<>();

    public JsonNode() {
    }

    public JsonNode(int value) {
    }

    public boolean isObject() {
        return true;
    }

    public void putField(String key, int value) {
        attributes.put(key, value);
    }

    public boolean hasField(String key) {
        return attributes.containsKey(key);
    }

    public int getInt(String key) {
        return attributes.get(key);
    }
}
