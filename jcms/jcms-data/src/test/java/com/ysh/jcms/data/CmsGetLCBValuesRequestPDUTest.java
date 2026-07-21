// Auto-generated. Tests for CmsGetLCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLCBValuesRequestPDU obj = new CmsGetLCBValuesRequestPDU();
        assertNotNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLCBValuesRequestPDU obj = new CmsGetLCBValuesRequestPDU();
        obj.reference = java.util.Collections.singletonList("test");
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLCBValuesRequestPDU d = MAPPER.readValue(json, CmsGetLCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
