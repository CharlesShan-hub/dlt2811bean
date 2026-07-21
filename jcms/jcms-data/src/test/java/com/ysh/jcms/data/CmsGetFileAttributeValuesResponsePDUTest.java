// Auto-generated. Tests for CmsGetFileAttributeValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileAttributeValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileAttributeValuesResponsePDU obj = new CmsGetFileAttributeValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetFileAttributeValuesResponsePDU obj = new CmsGetFileAttributeValuesResponsePDU(null);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileAttributeValuesResponsePDU obj = new CmsGetFileAttributeValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileAttributeValuesResponsePDU d = MAPPER.readValue(json, CmsGetFileAttributeValuesResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetFileAttributeValuesResponsePDU obj = new CmsGetFileAttributeValuesResponsePDU();
        byte[] data = obj.encode("uper");
        CmsGetFileAttributeValuesResponsePDU d = CmsGetFileAttributeValuesResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
