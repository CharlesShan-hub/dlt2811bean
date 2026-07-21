// Auto-generated. Tests for CmsGetFileAttributeValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileAttributeValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileAttributeValuesErrorPDU obj = new CmsGetFileAttributeValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetFileAttributeValuesErrorPDU obj = new CmsGetFileAttributeValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileAttributeValuesErrorPDU obj = new CmsGetFileAttributeValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileAttributeValuesErrorPDU d = MAPPER.readValue(json, CmsGetFileAttributeValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetFileAttributeValuesErrorPDU obj = new CmsGetFileAttributeValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetFileAttributeValuesErrorPDU d = CmsGetFileAttributeValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
