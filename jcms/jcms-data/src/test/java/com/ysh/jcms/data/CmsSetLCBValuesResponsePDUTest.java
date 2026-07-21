// Auto-generated. Tests for CmsSetLCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetLCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetLCBValuesResponsePDU obj = new CmsSetLCBValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetLCBValuesResponsePDU obj = new CmsSetLCBValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetLCBValuesResponsePDU obj = new CmsSetLCBValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetLCBValuesResponsePDU d = MAPPER.readValue(json, CmsSetLCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
