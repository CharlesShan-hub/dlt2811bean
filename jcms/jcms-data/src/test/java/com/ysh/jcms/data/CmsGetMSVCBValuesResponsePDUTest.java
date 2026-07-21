// Auto-generated. Tests for CmsGetMSVCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetMSVCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetMSVCBValuesResponsePDU obj = new CmsGetMSVCBValuesResponsePDU();
        assertNull(obj.msvcb);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetMSVCBValuesResponsePDU obj = new CmsGetMSVCBValuesResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetMSVCBValuesResponsePDU d = MAPPER.readValue(json, CmsGetMSVCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetMSVCBValuesResponsePDU obj = new CmsGetMSVCBValuesResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetMSVCBValuesResponsePDU d = CmsGetMSVCBValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
