// Auto-generated. Tests for CmsGetURCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetURCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetURCBValuesRequestPDU obj = new CmsGetURCBValuesRequestPDU();
        assertNotNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetURCBValuesRequestPDU obj = new CmsGetURCBValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetURCBValuesRequestPDU d = MAPPER.readValue(json, CmsGetURCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
