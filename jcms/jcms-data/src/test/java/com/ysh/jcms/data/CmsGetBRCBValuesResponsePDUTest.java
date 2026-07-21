// Auto-generated. Tests for CmsGetBRCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetBRCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetBRCBValuesResponsePDU obj = new CmsGetBRCBValuesResponsePDU();
        assertNull(obj.brcb);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetBRCBValuesResponsePDU obj = new CmsGetBRCBValuesResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetBRCBValuesResponsePDU d = MAPPER.readValue(json, CmsGetBRCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetBRCBValuesResponsePDU obj = new CmsGetBRCBValuesResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetBRCBValuesResponsePDU d = CmsGetBRCBValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
