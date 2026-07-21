// Auto-generated. Tests for CmsGetBRCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetBRCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetBRCBValuesErrorPDU obj = new CmsGetBRCBValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetBRCBValuesErrorPDU obj = new CmsGetBRCBValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetBRCBValuesErrorPDU obj = new CmsGetBRCBValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetBRCBValuesErrorPDU d = MAPPER.readValue(json, CmsGetBRCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetBRCBValuesErrorPDU obj = new CmsGetBRCBValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetBRCBValuesErrorPDU d = CmsGetBRCBValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
