// Auto-generated. Tests for CmsSetMSVCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetMSVCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetMSVCBValuesRequestPDU obj = new CmsSetMSVCBValuesRequestPDU();
        assertNull(obj.msvcb);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetMSVCBValuesRequestPDU obj = new CmsSetMSVCBValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetMSVCBValuesRequestPDU d = MAPPER.readValue(json, CmsSetMSVCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
