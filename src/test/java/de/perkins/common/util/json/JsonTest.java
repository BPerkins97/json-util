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
        Assertions.assertEquals(1, node.getInt("value"));
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
        Assertions.assertEquals(2, node.getInt("value"));
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
        Assertions.assertEquals(2, node.getInt("key"));
    }

    @Test
    void shouldThrowExceptionWhenNotStartingWithCurlyBrace() {
        String json = """
                            "key": 2
                        }
                        """;

        Assertions.assertThrows(IllegalArgumentException.class, () -> Json.parse(json));
    }
}
