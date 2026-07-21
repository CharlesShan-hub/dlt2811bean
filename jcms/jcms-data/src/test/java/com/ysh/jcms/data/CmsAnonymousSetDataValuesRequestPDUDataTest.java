// Auto-generated. Tests for CmsAnonymousSetDataValuesRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetDataValuesRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetDataValuesRequestPDUData obj = new CmsAnonymousSetDataValuesRequestPDUData();
        assertNull(obj.reference);
        assertNull(obj.fc);
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetDataValuesRequestPDUData obj = new CmsAnonymousSetDataValuesRequestPDUData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetDataValuesRequestPDUData d = MAPPER.readValue(json, CmsAnonymousSetDataValuesRequestPDUData.class);
        assertEquals(obj, d);
    }
}
