package de.perkins.common.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonNode {
    private Map<String, Object> attributes;
    private List<Object> list;
    private final JsonNodeType type;

    public JsonNode(JsonNodeType type) {
        this.type = type;
        if (type == JsonNodeType.OBJECT) {
             attributes = new HashMap<>();
        }
        if (type == JsonNodeType.ARRAY) {
            list = new ArrayList<>();
        }
    }

    public boolean isObject() {
        return JsonNodeType.OBJECT.equals(type);
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

    public boolean isArray() {
        return JsonNodeType.ARRAY.equals(type);
    }

    public int size() {
        return list.size();
    }

    public void addValue(Object value) {
        list.add(value);
    }

    public String getStringAt(int index) {
        return (String) list.get(index);
    }

    public double getNumberAt(int index) {
        return (double) list.get(index);
    }

    public boolean getBooleanAt(int index) {
        return (boolean) list.get(index);
    }

    public boolean isNullAt(int index) {
        return list.get(index) == null;
    }
}
