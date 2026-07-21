// Auto-generated. Tests for CmsGetFileErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileErrorPDU obj = new CmsGetFileErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetFileErrorPDU obj = new CmsGetFileErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileErrorPDU obj = new CmsGetFileErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileErrorPDU d = MAPPER.readValue(json, CmsGetFileErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetFileErrorPDU obj = new CmsGetFileErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetFileErrorPDU d = CmsGetFileErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
