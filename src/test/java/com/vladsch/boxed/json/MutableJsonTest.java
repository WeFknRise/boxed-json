/*
 * Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.boxed.json;

import com.vladsch.flexmark.util.misc.DelimitedBuilder;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.vladsch.boxed.json.BoxedJsValue.HAD_INVALID_LITERAL;
import static com.vladsch.boxed.json.BoxedJsValue.HAD_INVALID_NUMBER;
import static com.vladsch.boxed.json.BoxedJsValue.HAD_INVALID_STRING;
import static com.vladsch.boxed.json.BoxedJsValue.HAD_MISSING_LITERAL;
import static com.vladsch.boxed.json.BoxedJsValue.HAD_MISSING_NUMBER;
import static com.vladsch.boxed.json.BoxedJsValue.HAD_MISSING_STRING;
import static com.vladsch.boxed.json.BoxedJsValue.HAD_NULL_NUMBER;
import static com.vladsch.boxed.json.BoxedJsValue.HAD_NULL_STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MutableJsonTest {

    @Test
    public void test_Basic() {

        @Language("Json")
        String s = "{" +
                "  \"null\": null," +
                "  \"int0\": 0," +
                "  \"int1\": 1," +
                "  \"name\": \"value\"," +
                "  \"option\": \"optValue\"," +
                "  \"array\": [" +
                "  \"0\"," +
                "  \"1\"," +
                "  \"2\"," +
                "  \"3\"" +
                "  ]," +
                "  \"object\": {" +
                "    \"param0\": \"paramValue0\"," +
                "    \"param1\": \"paramValue1\"," +
                "    \"param2\": \"paramValue2\"," +
                "    \"param3\": \"paramValue3\"," +
                "    \"param4\": \"paramValue4\"," +
                "    \"param5\": \"paramValue5\"," +
                "    \"param6\": \"paramValue6\"," +
                "    \"param7\": \"paramValue7\"," +
                "    \"param8\": \"paramValue8\"," +
                "    \"0\": \"indexValue0\"," +
                "    \"1\": \"indexValue1\"," +
                "    \"2\": \"indexValue2\"," +
                "    \"3\": \"indexValue3\"," +
                "    \"4\": \"indexValue4\"," +
                "    \"5\": \"indexValue5\"," +
                "    \"6\": \"indexValue6\"," +
                "    \"7\": \"indexValue7\"," +
                "    \"8\": \"indexValue8\"," +
                "    \"array\": [" +
                "      \"0\"," +
                "      \"1\"," +
                "      \"2\"," +
                "      \"3\"," +
                "      4," +
                "      5," +
                "      6" +
                "    ]" +
                "  }" +
                "}";

        BoxedJsObject boxedJsonObject = BoxedJson.boxedFrom(s);
        BoxedJsObject jsonObject = BoxedJson.boxedFrom(s);

        assertEquals(0, jsonObject.evalInt("int0"));
        assertEquals(1, jsonObject.evalInt("int1"));
        assertFalse(jsonObject.isNull("int1"));
        assertTrue(jsonObject.isNull("null"));
        assertEquals("value", jsonObject.evalString("name"));
        assertEquals(HAD_MISSING_LITERAL, jsonObject.get("names"));

        assertEquals("\"paramValue2\"", jsonObject.evalJsString("object.param2").toString());
        assertEquals("\"indexValue4\"", jsonObject.evalJsString("object.4").toString());
        assertEquals("indexValue4", jsonObject.evalString("object.4"));
        assertEquals(HAD_INVALID_NUMBER, jsonObject.evalJsNumber("object.4"));
        assertEquals(0, jsonObject.evalInt("object.4"));
        assertEquals(HAD_MISSING_STRING, jsonObject.evalJsString("object.10"));
        assertEquals(HAD_INVALID_LITERAL, jsonObject.evalJsString("object.array.1"));
        assertEquals("1", jsonObject.evalString("object.array[1]"));
        assertEquals(HAD_INVALID_STRING, jsonObject.evalJsString("object.array[5]"));
        assertEquals(5, jsonObject.evalInt("object.array[5]"));
        assertEquals(HAD_INVALID_NUMBER, jsonObject.evalJsNumber("object.array[1]"));
        assertEquals(HAD_MISSING_NUMBER, jsonObject.evalJsNumber("object.array[10]"));
        assertEquals(HAD_NULL_NUMBER, jsonObject.evalJsNumber("null.array[10]"));
        assertEquals(HAD_NULL_STRING, jsonObject.evalJsString("null.array[10]"));

        jsonObject = BoxedJson.boxedFrom(s);
        jsonObject.evalSet("object.array[7].obj.name", 10);

        assertEquals("{\"null\":null,\"int0\":0,\"int1\":1,\"name\":\"value\",\"option\":\"optValue\",\"array\":[\"0\",\"1\",\"2\",\"3\"],\"object\":{\"param0\":\"paramValue0\",\"param1\":\"paramValue1\",\"param2\":\"paramValue2\",\"param3\":\"paramValue3\",\"param4\":\"paramValue4\",\"param5\":\"paramValue5\",\"param6\":\"paramValue6\",\"param7\":\"paramValue7\",\"param8\":\"paramValue8\",\"0\":\"indexValue0\",\"1\":\"indexValue1\",\"2\":\"indexValue2\",\"3\":\"indexValue3\",\"4\":\"indexValue4\",\"5\":\"indexValue5\",\"6\":\"indexValue6\",\"7\":\"indexValue7\",\"8\":\"indexValue8\",\"array\":[\"0\",\"1\",\"2\",\"3\",4,5,6,{\"obj\":{\"name\":10}}]}}",
                jsonObject.toString());

        jsonObject = BoxedJson.boxedFrom(s);
        jsonObject.evalSet("object.array[].obj.name", 10);

        assertEquals("{\"null\":null,\"int0\":0,\"int1\":1,\"name\":\"value\",\"option\":\"optValue\",\"array\":[\"0\",\"1\",\"2\",\"3\"],\"object\":{\"param0\":\"paramValue0\",\"param1\":\"paramValue1\",\"param2\":\"paramValue2\",\"param3\":\"paramValue3\",\"param4\":\"paramValue4\",\"param5\":\"paramValue5\",\"param6\":\"paramValue6\",\"param7\":\"paramValue7\",\"param8\":\"paramValue8\",\"0\":\"indexValue0\",\"1\":\"indexValue1\",\"2\":\"indexValue2\",\"3\":\"indexValue3\",\"4\":\"indexValue4\",\"5\":\"indexValue5\",\"6\":\"indexValue6\",\"7\":\"indexValue7\",\"8\":\"indexValue8\",\"array\":[\"0\",\"1\",\"2\",\"3\",4,5,6,{\"obj\":{\"name\":10}}]}}",
                jsonObject.toString());

        jsonObject = BoxedJson.boxedFrom(s);
        jsonObject.evalSet("object.array[].obj[].name", 10);

        assertEquals("{\"null\":null,\"int0\":0,\"int1\":1,\"name\":\"value\",\"option\":\"optValue\",\"array\":[\"0\",\"1\",\"2\",\"3\"],\"object\":{\"param0\":\"paramValue0\",\"param1\":\"paramValue1\",\"param2\":\"paramValue2\",\"param3\":\"paramValue3\",\"param4\":\"paramValue4\",\"param5\":\"paramValue5\",\"param6\":\"paramValue6\",\"param7\":\"paramValue7\",\"param8\":\"paramValue8\",\"0\":\"indexValue0\",\"1\":\"indexValue1\",\"2\":\"indexValue2\",\"3\":\"indexValue3\",\"4\":\"indexValue4\",\"5\":\"indexValue5\",\"6\":\"indexValue6\",\"7\":\"indexValue7\",\"8\":\"indexValue8\",\"array\":[\"0\",\"1\",\"2\",\"3\",4,5,6,{\"obj\":[{\"name\":10}]}]}}",
                jsonObject.toString());

        // should not change if invalid down the list
        jsonObject = BoxedJson.boxedFrom(s);
        jsonObject.evalSet("object.array[].obj[10].name", 10);

        assertEquals(boxedJsonObject.toString(), jsonObject.toString());
    }

    String path(Object[] parts) {
        if (parts == null) return null;

        DelimitedBuilder sb = new DelimitedBuilder("|");
        for (Object part : parts) {
            if (part instanceof String) sb.append("'").append(part).append("'").mark();
            else if (part instanceof Integer) sb.append(part).mark();
            else sb.append(part.getClass().getSimpleName()).mark();
        }
        return sb.toString();
    }

    @Test
    public void test_path() {
        assertEquals("'obj1'", path(BoxedJson.parseEvalPath("obj1", false)));
        assertEquals("'obj1'|'obj2'", path(BoxedJson.parseEvalPath("obj1.obj2", false)));
        assertEquals("'obj1'|2", path(BoxedJson.parseEvalPath("obj1[2]", false)));
        assertEquals("1|'obj2'", path(BoxedJson.parseEvalPath("[1].obj2", false)));
        assertEquals("1|2", path(BoxedJson.parseEvalPath("[1][2]", false)));
        assertEquals("-1", path(BoxedJson.parseEvalPath("[]", true)));
        assertEquals("-1|2", path(BoxedJson.parseEvalPath("[][2]", true)));
        assertEquals("-1|-1", path(BoxedJson.parseEvalPath("[][]", true)));
        assertEquals("-1|'a'", path(BoxedJson.parseEvalPath("[].a", true)));
        assertEquals("'a'|-1", path(BoxedJson.parseEvalPath("a[]", true)));
        assertEquals("'object'|'array'|-1|'obj'|-1|'name'", path(BoxedJson.parseEvalPath("object.array[].obj[].name", true)));

        // invalid paths
        assertNull(path(BoxedJson.parseEvalPath("[1]obj2", false)));
        assertNull(path(BoxedJson.parseEvalPath("[]", false)));
        assertNull(path(BoxedJson.parseEvalPath("abc..def", false)));
        assertNull(path(BoxedJson.parseEvalPath(".def", false)));
        assertNull(path(BoxedJson.parseEvalPath("[", false)));
        assertNull(path(BoxedJson.parseEvalPath("]", false)));
        assertNull(path(BoxedJson.parseEvalPath("ab.", false)));
        assertNull(path(BoxedJson.parseEvalPath("ab[.", false)));
    }

    @Test
    public void test_mods() {
        BoxedJsObject json = BoxedJson.boxedFrom("{\"method\":\"Page.frameStartedLoading\",\"params\":{\"frameId\":\"0.1\"}}");

        @NotNull BoxedJsString frameId = json.eval("params.frameId").asJsString();
        
        if (frameId.isValid() && frameId.getString().equals("0.1")) {
            // add an array of positions to params
            MutableJsObject jsObject = new MutableJsObject();
            jsObject.put("x", 100);
            jsObject.put("y", 5);
            json.evalSet("params.positions[]", jsObject);
            json.evalSet("params.frameId", "1.0");
        }
        
        String jsonText = json.toString();
        assertEquals("{\"method\":\"Page.frameStartedLoading\",\"params\":{\"frameId\":\"1.0\",\"positions\":[{\"x\":100,\"y\":5}]}}", jsonText);
    }
}

