// Auto-generated. Tests for CmsGetAllDataValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataValuesResponsePDU obj = new CmsGetAllDataValuesResponsePDU();
        assertNull(obj.data);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataValuesResponsePDU obj = new CmsGetAllDataValuesResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataValuesResponsePDU d = MAPPER.readValue(json, CmsGetAllDataValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllDataValuesResponsePDU obj = new CmsGetAllDataValuesResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetAllDataValuesResponsePDU d = CmsGetAllDataValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
