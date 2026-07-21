// Auto-generated. Tests for CmsGetBRCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetBRCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetBRCBValuesRequestPDU obj = new CmsGetBRCBValuesRequestPDU();
        assertNotNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetBRCBValuesRequestPDU obj = new CmsGetBRCBValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetBRCBValuesRequestPDU d = MAPPER.readValue(json, CmsGetBRCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetBRCBValuesRequestPDU obj = new CmsGetBRCBValuesRequestPDU();
        byte[] data = obj.encode("uper");
        CmsGetBRCBValuesRequestPDU d = CmsGetBRCBValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
