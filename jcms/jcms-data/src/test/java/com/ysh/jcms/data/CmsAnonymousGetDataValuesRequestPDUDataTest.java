// Auto-generated. Tests for CmsAnonymousGetDataValuesRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetDataValuesRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetDataValuesRequestPDUData obj = new CmsAnonymousGetDataValuesRequestPDUData();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetDataValuesRequestPDUData obj = new CmsAnonymousGetDataValuesRequestPDUData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetDataValuesRequestPDUData d = MAPPER.readValue(json, CmsAnonymousGetDataValuesRequestPDUData.class);
        assertEquals(obj, d);
    }
}
