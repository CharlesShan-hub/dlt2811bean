// Auto-generated. Tests for CmsSetMSVCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetMSVCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetMSVCBValuesErrorPDU obj = new CmsSetMSVCBValuesErrorPDU();
        assertNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetMSVCBValuesErrorPDU obj = new CmsSetMSVCBValuesErrorPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetMSVCBValuesErrorPDU d = MAPPER.readValue(json, CmsSetMSVCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetMSVCBValuesErrorPDU obj = new CmsSetMSVCBValuesErrorPDU();
        byte[] data = obj.encode("uper");
        CmsSetMSVCBValuesErrorPDU d = CmsSetMSVCBValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
