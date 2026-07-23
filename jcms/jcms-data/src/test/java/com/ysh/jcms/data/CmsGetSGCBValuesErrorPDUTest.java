// Auto-generated. Tests for CmsGetSGCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetSGCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetSGCBValuesErrorPDU obj = new CmsGetSGCBValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetSGCBValuesErrorPDU obj = new CmsGetSGCBValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetSGCBValuesErrorPDU obj = new CmsGetSGCBValuesErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetSGCBValuesErrorPDU d = MAPPER.readValue(json, CmsGetSGCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetSGCBValuesErrorPDU obj = new CmsGetSGCBValuesErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetSGCBValuesErrorPDU d = CmsGetSGCBValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
