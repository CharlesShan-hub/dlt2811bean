// Auto-generated. Tests for CmsSetURCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetURCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetURCBValuesRequestPDU obj = new CmsSetURCBValuesRequestPDU();
        assertNull(obj.urcb);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetURCBValuesRequestPDU obj = new CmsSetURCBValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetURCBValuesRequestPDU d = MAPPER.readValue(json, CmsSetURCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
