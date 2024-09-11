package de.perkins.common.util.json;

public class Json {
    private static final int MODE_WHITESPACE_CONSUME = 0;
    private static final int MODE_KEY = 1;
    private static final int MODE_OBJECT_START = 2;
    private static final int MODE_ASSIGNMENT = 3;
    private static final int MODE_VALUE = 4;

    public static JsonNode parse(String json) {
        if (json.codePointAt(0) != '{') {
            throw new IllegalArgumentException("A valid JSON has to start with a curly brace '{'.");
        }

        int previousMode = MODE_OBJECT_START;
        int mode = MODE_WHITESPACE_CONSUME;
        int numCodePoints = json.codePointCount(0, json.length());
        JsonNode node = new JsonNode();
        StringBuilder builder = new StringBuilder();
        String value = null;

        for (int i=1;i<numCodePoints;i++) {
            int codePoint = json.codePointAt(i);
            switch (mode) {
                case MODE_WHITESPACE_CONSUME -> {
                    if (!isWhitespace(codePoint)) {
                        switch (previousMode) {
                            case MODE_OBJECT_START -> {
                                // TODO throw exception if this char is not a quote
                                previousMode = MODE_WHITESPACE_CONSUME;
                                mode = MODE_KEY;
                            }
                            case MODE_KEY -> {
                                // TODO throw exception if this char is not a :
                                previousMode = MODE_ASSIGNMENT;
                            }
                            case MODE_ASSIGNMENT -> {
                                mode = MODE_VALUE;
                                builder.appendCodePoint(codePoint);
                            }
                        }
                        if (previousMode == MODE_OBJECT_START) {

                        }
                    }
                }
                case MODE_KEY -> {
                    if (codePoint == '"' && json.codePointAt(i-1) != '\\') {
                        previousMode = MODE_KEY;
                        mode = MODE_WHITESPACE_CONSUME;
                        value = builder.toString();
                        builder = new StringBuilder();
                    } else {
                        builder.appendCodePoint(codePoint);
                    }
                }
                case MODE_VALUE -> {
                    if (isWhitespace(codePoint)) {
                        previousMode = MODE_VALUE;
                        mode = MODE_WHITESPACE_CONSUME;
                        node.putField(value, Integer.parseInt(builder.toString()));
                    } else {
                        builder.appendCodePoint(codePoint);
                    }
                }
            }
        }
        return node;
    }

    private static boolean isWhitespace(int codePoint) {
        return codePoint == ' '
                || codePoint == '\t'
                || codePoint == '\n'
                || codePoint == '\r';
    }
}
