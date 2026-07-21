// Auto-generated. Tests for CmsSetLCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetLCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetLCBValuesErrorPDU obj = new CmsSetLCBValuesErrorPDU();
        assertNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetLCBValuesErrorPDU obj = new CmsSetLCBValuesErrorPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetLCBValuesErrorPDU d = MAPPER.readValue(json, CmsSetLCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetLCBValuesErrorPDU obj = new CmsSetLCBValuesErrorPDU();
        byte[] data = obj.encode("uper");
        CmsSetLCBValuesErrorPDU d = CmsSetLCBValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
