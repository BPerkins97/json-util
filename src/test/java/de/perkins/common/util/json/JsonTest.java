package de.perkins.common.util.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonTest {
    @Test
    void shouldParseInt() {
        String json = """
                        {
                            "value": 1
                        }
                        """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isObject());
        Assertions.assertTrue(node.hasField("value"));
        Assertions.assertEquals(1, node.getNumber("value"));
    }

    @Test
    void shouldParseInt2() {
        String json = """
                        {
                            "value": 2
                        }
                        """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isObject());
        Assertions.assertTrue(node.hasField("value"));
        Assertions.assertEquals(2, node.getNumber("value"));
    }

    @Test
    void shouldParseKey() {
        String json = """
                        {
                            "key": 2
                        }
                        """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isObject());
        Assertions.assertFalse(node.hasField("value"));
        Assertions.assertTrue(node.hasField("key"));
        Assertions.assertEquals(2, node.getNumber("key"));
    }

    @Test
    void shouldThrowExceptionWhenNotStartingWithCurlyBrace() {
        String json = """
                            "key": 2
                        }
                        """;

        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> Json.parse(json));
        Assertions.assertEquals("A valid JSON has to start with a curly brace '{'.", illegalArgumentException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenKeyIsNotFollowedByColon() {
        String json = """
                       {
                            "key" 2
                        }
                        """;

        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> Json.parse(json));
        Assertions.assertEquals("A key definition has to be followed by a colon.", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNotQuotingAKey() {
        String json = """
                       {
                            key" 2
                        }
                        """;

        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> Json.parse(json));
        Assertions.assertEquals("After object declaration the object has to either be closed or have a field declaration.", illegalArgumentException.getMessage());
    }

    @Test
    public void shouldParseTwoFields() {
        String json = """
                       {
                            "key1": 2,
                            "key2": 3
                        }
                        """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isObject());
        Assertions.assertEquals(2, node.getNumber("key1"));
        Assertions.assertEquals(3, node.getNumber("key2"));
    }

    @Test
    public void shouldIgnoreLeadingWhitespace() {
        String json = """
                       \n\t\r {
                            "key1": 2
                        }
                        """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isObject());
        Assertions.assertTrue(node.hasField("key1"));
        Assertions.assertEquals(2, node.getNumber("key1"));
    }

    @Test
    public void shouldThrowExceptionWhenNotClosingWithCurlyBrace() {
        String json = """
                       {
                            "key1": 2
                        """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isObject());
        Assertions.assertTrue(node.hasField("key1"));
        Assertions.assertEquals(2, node.getNumber("key1"));
    }
}
