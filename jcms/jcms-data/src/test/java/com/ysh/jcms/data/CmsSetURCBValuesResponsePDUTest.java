// Auto-generated. Tests for CmsSetURCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetURCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetURCBValuesResponsePDU obj = new CmsSetURCBValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetURCBValuesResponsePDU obj = new CmsSetURCBValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetURCBValuesResponsePDU obj = new CmsSetURCBValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetURCBValuesResponsePDU d = MAPPER.readValue(json, CmsSetURCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetURCBValuesResponsePDU obj = new CmsSetURCBValuesResponsePDU();
        byte[] data = obj.encode("uper");
        CmsSetURCBValuesResponsePDU d = CmsSetURCBValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
