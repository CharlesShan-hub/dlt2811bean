// Auto-generated. Tests for CmsSetGoCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetGoCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetGoCBValuesResponsePDU obj = new CmsSetGoCBValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetGoCBValuesResponsePDU obj = new CmsSetGoCBValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetGoCBValuesResponsePDU obj = new CmsSetGoCBValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetGoCBValuesResponsePDU d = MAPPER.readValue(json, CmsSetGoCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetGoCBValuesResponsePDU obj = new CmsSetGoCBValuesResponsePDU();
        byte[] data = obj.encode("uper");
        CmsSetGoCBValuesResponsePDU d = CmsSetGoCBValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
