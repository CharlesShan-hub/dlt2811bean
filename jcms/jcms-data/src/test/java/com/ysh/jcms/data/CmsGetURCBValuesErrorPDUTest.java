// Auto-generated. Tests for CmsGetURCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetURCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetURCBValuesErrorPDU obj = new CmsGetURCBValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetURCBValuesErrorPDU obj = new CmsGetURCBValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetURCBValuesErrorPDU obj = new CmsGetURCBValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetURCBValuesErrorPDU d = MAPPER.readValue(json, CmsGetURCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetURCBValuesErrorPDU obj = new CmsGetURCBValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetURCBValuesErrorPDU d = CmsGetURCBValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
