// Auto-generated. Tests for CmsGetAllCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllCBValuesErrorPDU obj = new CmsGetAllCBValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetAllCBValuesErrorPDU obj = new CmsGetAllCBValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllCBValuesErrorPDU obj = new CmsGetAllCBValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllCBValuesErrorPDU d = MAPPER.readValue(json, CmsGetAllCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllCBValuesErrorPDU obj = new CmsGetAllCBValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetAllCBValuesErrorPDU d = CmsGetAllCBValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
