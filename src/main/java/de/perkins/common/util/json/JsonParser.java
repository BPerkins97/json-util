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
        if (next() == '[') {
            return parseArray();
        }
        throw new IllegalArgumentException("A valid JSON has to start with a curly brace '{'.");
    }

    private JsonNode parseArray() {
        consumeToken('[', "A JSON Array has to start with '['.");
        eatWhitespace();
        JsonNode node = new JsonNode(JsonNodeType.ARRAY);
        boolean forceLast = false;
        while (next() != ']' && !forceLast) {
            node.addValue(parseValue());
            eatWhitespace();
            if (next() != ',') {
                forceLast = true;
            } else {
                consume();
                eatWhitespace();
            }
        }
        consumeToken(']', "A JSON Array has to end with ']'.");
        return node;
    }

    private JsonNode parseObject() {
        consumeToken('{', "A JSON Object has to start with '{'.");
        eatWhitespace();
        JsonNode node = new JsonNode(JsonNodeType.OBJECT);
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
                    consume();
                    eatWhitespace();
                }
                default -> throw new IllegalArgumentException("After object declaration the object has to either be closed or have a field declaration.");
            }
        }
        consumeToken('}', "A JSON Object has to end with '}'.");
        return node;
    }

    private Object parseValue() {
        if (next() == '"') {
            return parseString();
        }
        if (next() == '{') {
            return parseObject();
        }
        if (next() == '[') {
            return parseArray();
        }
        if (isNextNumber() || next() == '-') {
            return parseNumber();
        }
        if (isNextTrue()) {
            pointer += 4;
            return true;
        }
        if (isNextFalse()) {
            pointer += 5;
            return false;
        }
        if (isNextNull()) {
            pointer += 4;
            return null;
        }
        throw new IllegalArgumentException("No JSON value found.");
    }

    private boolean isNextNull() {
        return next() == 'n'
                && codePoint(pointer + 1) == 'u'
                && codePoint(pointer + 2) == 'l'
                && codePoint(pointer + 3) == 'l';
    }

    private boolean isNextTrue() {
        return next() == 't'
                && codePoint(pointer + 1) == 'r'
                && codePoint(pointer + 2) == 'u'
                && codePoint(pointer + 3) == 'e';
    }

    private boolean isNextFalse() {
        return next() == 'f'
                && codePoint(pointer + 1) == 'a'
                && codePoint(pointer + 2) == 'l'
                && codePoint(pointer + 3) == 's'
                && codePoint(pointer + 4) == 'e';
    }

    private boolean isNextNumber() {
        int codePoint = next();
        return codePoint >= '0' && codePoint <= '9';
    }

    private Double parseNumber() {
        StringBuilder builder = new StringBuilder();
        if (next() == '-') {
            builder.appendCodePoint(consume());
        }
        while (isNextNumber()) {
            builder.appendCodePoint(consume());
        }
        if (next() == '.') {
            builder.appendCodePoint(consume());
            if (!isNextNumber()) {
                throw new IllegalArgumentException();
            }
            while (isNextNumber()) {
                builder.appendCodePoint(consume());
            }
        }
        if (next() == 'e' || next() == 'E') {
            builder.appendCodePoint(consume());
            if (next() == '+' || next() == '-') {
                builder.appendCodePoint(consume());
            }
            if (!isNextNumber()) {
                throw new IllegalArgumentException();
            }
            while (isNextNumber()) {
                builder.appendCodePoint(consume());
            }
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
        return codePoint(pointer);
    }

    private int codePoint(int index) {
        if (index >= length) {
            throw new IllegalArgumentException("EOF reached before JSON structure was closed.");
        }
        return json.codePointAt(index);
    }

    private static boolean isWhitespace(int codePoint) {
        return codePoint == ' '
                || codePoint == '\t'
                || codePoint == '\n'
                || codePoint == '\r';
    }

    private int consume() {
        int codePoint = codePoint(pointer);
        pointer++;
        return codePoint;
    }
}
