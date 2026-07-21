// Auto-generated. Tests for CmsSetBRCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetBRCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetBRCBValuesResponsePDU obj = new CmsSetBRCBValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetBRCBValuesResponsePDU obj = new CmsSetBRCBValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetBRCBValuesResponsePDU obj = new CmsSetBRCBValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetBRCBValuesResponsePDU d = MAPPER.readValue(json, CmsSetBRCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetBRCBValuesResponsePDU obj = new CmsSetBRCBValuesResponsePDU();
        byte[] data = obj.encode("uper");
        CmsSetBRCBValuesResponsePDU d = CmsSetBRCBValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
