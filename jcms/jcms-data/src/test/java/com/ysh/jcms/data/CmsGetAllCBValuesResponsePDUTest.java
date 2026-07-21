// Auto-generated. Tests for CmsGetAllCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllCBValuesResponsePDU obj = new CmsGetAllCBValuesResponsePDU();
        assertNull(obj.cb_value);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllCBValuesResponsePDU obj = new CmsGetAllCBValuesResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllCBValuesResponsePDU d = MAPPER.readValue(json, CmsGetAllCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllCBValuesResponsePDU obj = new CmsGetAllCBValuesResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetAllCBValuesResponsePDU d = CmsGetAllCBValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
