// Auto-generated. Tests for CmsGetSGCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetSGCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetSGCBValuesRequestPDU obj = new CmsGetSGCBValuesRequestPDU();
        assertNotNull(obj.sgcb_reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetSGCBValuesRequestPDU obj = new CmsGetSGCBValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetSGCBValuesRequestPDU d = MAPPER.readValue(json, CmsGetSGCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
