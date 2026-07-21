// Auto-generated. Tests for CmsGetGoReferenceErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoReferenceErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoReferenceErrorPDU obj = new CmsGetGoReferenceErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetGoReferenceErrorPDU obj = new CmsGetGoReferenceErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoReferenceErrorPDU obj = new CmsGetGoReferenceErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoReferenceErrorPDU d = MAPPER.readValue(json, CmsGetGoReferenceErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetGoReferenceErrorPDU obj = new CmsGetGoReferenceErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetGoReferenceErrorPDU d = CmsGetGoReferenceErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
