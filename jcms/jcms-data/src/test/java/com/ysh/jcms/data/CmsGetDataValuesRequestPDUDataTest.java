// Auto-generated. Tests for CmsGetDataValuesRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataValuesRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataValuesRequestPDUData obj = new CmsGetDataValuesRequestPDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataValuesRequestPDUData obj = new CmsGetDataValuesRequestPDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataValuesRequestPDUData d = MAPPER.readValue(json, CmsGetDataValuesRequestPDUData.class);
        assertEquals(obj, d);
    }
}
