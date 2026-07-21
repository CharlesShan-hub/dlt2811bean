// Auto-generated. Tests for CmsGetLCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLCBValuesErrorPDU obj = new CmsGetLCBValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetLCBValuesErrorPDU obj = new CmsGetLCBValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLCBValuesErrorPDU obj = new CmsGetLCBValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLCBValuesErrorPDU d = MAPPER.readValue(json, CmsGetLCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLCBValuesErrorPDU obj = new CmsGetLCBValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetLCBValuesErrorPDU d = CmsGetLCBValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
