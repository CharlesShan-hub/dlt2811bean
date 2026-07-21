// Auto-generated. Tests for CmsGetAllCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllCBValuesRequestPDU obj = new CmsGetAllCBValuesRequestPDU();
        assertNull(obj.reference);
        assertEquals(0, obj.acsi_class);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllCBValuesRequestPDU obj = new CmsGetAllCBValuesRequestPDU();
        obj.acsi_class = 42;
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllCBValuesRequestPDU d = MAPPER.readValue(json, CmsGetAllCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllCBValuesRequestPDU obj = new CmsGetAllCBValuesRequestPDU();
        obj.acsi_class = 42;
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetAllCBValuesRequestPDU d = CmsGetAllCBValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
