// Auto-generated. Tests for CmsSetLCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetLCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetLCBValuesRequestPDU obj = new CmsSetLCBValuesRequestPDU();
        assertNull(obj.lcb);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetLCBValuesRequestPDU obj = new CmsSetLCBValuesRequestPDU();
        obj.lcb = java.util.Collections.singletonList(new CmsAnonymousSetLCBValuesRequestPDULcb());
        String json = MAPPER.writeValueAsString(obj);
        CmsSetLCBValuesRequestPDU d = MAPPER.readValue(json, CmsSetLCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
