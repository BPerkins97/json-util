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
        Assertions.assertFalse(node.isArray());
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

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> Json.parse(json));
        Assertions.assertEquals("EOF reached before JSON structure was closed.", e.getMessage());
    }

    @Test
    public void shouldPassOnEmptyObject() {
        String json = "{}";

        JsonNode node = Json.parse(json);
        Assertions.assertNotNull(node);
    }

    @Test
    public void shouldParseNestedObject() {
        String json = """
                {
                     "key1": {
                         "key2": 3
                     }
                 }
                """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isObject());
        Assertions.assertNotNull(node.getObject("key1"));
        Assertions.assertTrue(node.getObject("key1").isObject());
        Assertions.assertEquals(3, node.getObject("key1").getNumber("key2"));
    }

    @Test
    public void shouldParseEmptyArray() {
        String json = """
                []
                """;

        JsonNode node = Json.parse(json);
        Assertions.assertFalse(node.isObject());
        Assertions.assertTrue(node.isArray());
        Assertions.assertEquals(0, node.size());
    }

    @Test
    public void shouldParseArrayWithOneValue() {
        String json = """
                [
                1
                ]
                """;

        JsonNode node = Json.parse(json);
        Assertions.assertFalse(node.isObject());
        Assertions.assertTrue(node.isArray());
        Assertions.assertEquals(1, node.size());
        Assertions.assertEquals(1, node.getNumberAt(0));
    }

    @Test
    public void shouldParseArrayWithTwoValues() {
        String json = """
                [
                1, "2"
                ]
                """;

        JsonNode node = Json.parse(json);
        Assertions.assertFalse(node.isObject());
        Assertions.assertTrue(node.isArray());
        Assertions.assertEquals(2, node.size());
        Assertions.assertEquals(1, node.getNumberAt(0));
        Assertions.assertEquals("2", node.getStringAt(1));
    }

    @Test
    public void shouldParseBooleans() {
        String json = """
                [
                true,false
                ]
                """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isArray());
        Assertions.assertEquals(2, node.size());
        Assertions.assertTrue(node.getBooleanAt(0));
        Assertions.assertFalse(node.getBooleanAt(1));
    }

    @Test
    public void shouldParseNull() {
        String json = """
                [
                null,false
                ]
                """;

        JsonNode node = Json.parse(json);
        Assertions.assertTrue(node.isArray());
        Assertions.assertEquals(2, node.size());
        Assertions.assertTrue(node.isNullAt(0));
        Assertions.assertFalse(node.getBooleanAt(1));
    }

    @Test
    public void shouldThrowExceptionWhenNoValueIsProvided() {
        String json = """
                [
                nxxx
                ]
                """;

        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> Json.parse(json));
        Assertions.assertEquals("No JSON value found.", e.getMessage());
    }

    @Test
    public void shouldParse() {
        String json = """
            {
            	"id": "0001",
            	"type": "donut",
            	"name": "Cake",
            	"ppu": 0.55,
            	"batters":
            		{
            			"batter":
            				[
            					{ "id": "1001", "type": "Regular" },
            					{ "id": "1002", "type": "Chocolate" },
            					{ "id": "1003", "type": "Blueberry" },
            					{ "id": "1004", "type": "Devil's Food" }
            				]
            		},
            	"topping":
            		[
            			{ "id": "5001", "type": "None" },
            			{ "id": "5002", "type": "Glazed" },
            			{ "id": "5005", "type": "Sugar" },
            			{ "id": "5007", "type": "Powdered Sugar" },
            			{ "id": "5006", "type": "Chocolate with Sprinkles" },
            			{ "id": "5003", "type": "Chocolate" },
            			{ "id": "5004", "type": "Maple" }
            		]
            }
            """;
        JsonNode node = Json.parse(json);
        Assertions.assertNotNull(node);
    }
}
