// Auto-generated. Tests for CmsTriggerConditions

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsTriggerConditionsTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsTriggerConditions obj = new CmsTriggerConditions();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsTriggerConditions obj = new CmsTriggerConditions(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsTriggerConditions obj = new CmsTriggerConditions(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsTriggerConditions d = MAPPER.readValue(json, CmsTriggerConditions.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsTriggerConditions obj = new CmsTriggerConditions(42);
        byte[] data = obj.encode("uper");
        CmsTriggerConditions d = CmsTriggerConditions.decode("uper", data);
        assertEquals(obj, d);
    }
}
