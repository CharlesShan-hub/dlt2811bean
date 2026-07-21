// Auto-generated. Tests for CmsGetURCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetURCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetURCBValuesResponsePDU obj = new CmsGetURCBValuesResponsePDU();
        assertNull(obj.urcb);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetURCBValuesResponsePDU obj = new CmsGetURCBValuesResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetURCBValuesResponsePDU d = MAPPER.readValue(json, CmsGetURCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetURCBValuesResponsePDU obj = new CmsGetURCBValuesResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetURCBValuesResponsePDU d = CmsGetURCBValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
