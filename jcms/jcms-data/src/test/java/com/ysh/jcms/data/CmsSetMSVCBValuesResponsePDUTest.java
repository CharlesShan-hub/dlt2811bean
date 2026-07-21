// Auto-generated. Tests for CmsSetMSVCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetMSVCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetMSVCBValuesResponsePDU obj = new CmsSetMSVCBValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetMSVCBValuesResponsePDU obj = new CmsSetMSVCBValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetMSVCBValuesResponsePDU obj = new CmsSetMSVCBValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetMSVCBValuesResponsePDU d = MAPPER.readValue(json, CmsSetMSVCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
