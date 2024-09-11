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
        if (next() == '{') {
            return parseObject();
        }
        throw new IllegalArgumentException("A valid JSON has to start with a curly brace '{'.");
    }

    private JsonNode parseObject() {
        consumeToken('{', "A valid JSON has to start with a curly brace '{'.");
        eatWhitespace();
        JsonNode node = new JsonNode();
        while (next() != '}') {
            switch (next()) {
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
        if (next() == '"') {
            return parseString();
        }
        if (next() == '{') {
            return parseObject();
        }
        if (isNumber(next())) {
            return parseNumber();
        }
        throw new UnsupportedOperationException();
    }

    private boolean isNumber(int codePoint) {
        return codePoint >= '0' && codePoint <= '9';
    }

    private Double parseNumber() {
        StringBuilder builder = new StringBuilder();
        while (isNumber(next())) {
            builder.appendCodePoint(next());
            pointer++;
        }
        return Double.parseDouble(builder.toString());
    }

    private String parseString() {
        consumeToken('"', "A key has to be quoted.");
        StringBuilder builder = new StringBuilder();
        while (next() != '"') {
            builder.appendCodePoint(next());
            pointer++;
        }
        consumeToken('"', "A key has to be quoted.");
        return builder.toString();
    }

    private void consumeToken(int tokenCodePoint, String errorMessage) {
        if (next() != tokenCodePoint) {
            throw new IllegalArgumentException(errorMessage);
        }
        pointer++;
    }

    private void eatWhitespace() {
        while (isWhitespace(next())) {
            pointer++;
        }
    }

    private int next() {
        if (pointer >= length) {
            throw new IllegalArgumentException("EOF reached before JSON structure was closed.");
        }
        return json.codePointAt(pointer);
    }

    private static boolean isWhitespace(int codePoint) {
        return codePoint == ' '
                || codePoint == '\t'
                || codePoint == '\n'
                || codePoint == '\r';
    }
}
