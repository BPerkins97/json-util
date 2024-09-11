package de.perkins.common.util.json;

class JsonParser {
    private int pointer = 0;
    private final int length;
    private final String json;

    JsonParser(String json) {
        this.json = json;
        this.length = json.codePointCount(0, json.length());
    }

    public JsonNode parse() {
        eatWhitespace();
        if (json.codePointAt(pointer) == '{') {
            return parseObject();
        }
        throw new UnsupportedOperationException();
    }

    private JsonNode parseObject() {
        consumeToken('{', "A valid JSON has to start with a curly brace '{'.");
        eatWhitespace();
        JsonNode node = new JsonNode();
        while (json.codePointAt(pointer) != '}') {
            switch (json.codePointAt(pointer)) {
                case '"' -> {
                    String key = parseString();
                    eatWhitespace();
                    consumeToken(':', "A key definition has to be followed by a colon.");
                    eatWhitespace();
                    Object value = parseValue();
                    node.putField(key, value);
                    eatWhitespace();
                }
                case ',' -> {
                    pointer++;
                    eatWhitespace();
                }
                default -> {
                    throw new IllegalArgumentException("After object declaration the object has to either be closed or have a field declaration.");
                }
            }
        }
        return node;
    }

    private Object parseValue() {
        if (json.codePointAt(pointer) == '"') {
            return parseString();
        }
        if (isNumber(json.codePointAt(pointer))) {
            return parseNumber();
        }
        throw new UnsupportedOperationException();
    }

    private boolean isNumber(int codePoint) {
        return codePoint >= '0' && codePoint <= '9';
    }

    private Double parseNumber() {
        StringBuilder builder = new StringBuilder();
        while (isNumber(json.codePointAt(pointer))) {
            builder.appendCodePoint(json.codePointAt(pointer));
            pointer++;
        }
        return Double.parseDouble(builder.toString());
    }

    private String parseString() {
        consumeToken('"', "A key has to be quoted.");
        StringBuilder builder = new StringBuilder();
        while (json.codePointAt(pointer) != '"') {
            builder.appendCodePoint(json.codePointAt(pointer));
            pointer++;
        }
        consumeToken('"', "A key has to be quoted.");
        return builder.toString();
    }

    private void consumeToken(int tokenCodePoint, String errorMessage) {
        if (json.codePointAt(pointer) != tokenCodePoint) {
            throw new IllegalArgumentException(errorMessage);
        }
        pointer++;
    }

    private void eatWhitespace() {
        while (isWhitespace(json.codePointAt(pointer))) {
            pointer++;
        }
    }

    private static boolean isWhitespace(int codePoint) {
        return codePoint == ' '
                || codePoint == '\t'
                || codePoint == '\n'
                || codePoint == '\r';
    }
}
