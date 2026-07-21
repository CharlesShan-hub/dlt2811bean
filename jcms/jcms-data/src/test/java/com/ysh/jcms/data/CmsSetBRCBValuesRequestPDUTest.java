// Auto-generated. Tests for CmsSetBRCBValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetBRCBValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetBRCBValuesRequestPDU obj = new CmsSetBRCBValuesRequestPDU();
        assertNull(obj.brcb);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetBRCBValuesRequestPDU obj = new CmsSetBRCBValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetBRCBValuesRequestPDU d = MAPPER.readValue(json, CmsSetBRCBValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetBRCBValuesRequestPDU obj = new CmsSetBRCBValuesRequestPDU();
        byte[] data = obj.encode("uper");
        CmsSetBRCBValuesRequestPDU d = CmsSetBRCBValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
