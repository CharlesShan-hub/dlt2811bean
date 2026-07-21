// Auto-generated. Tests for CmsSetMSVCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetMSVCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetMSVCBValuesErrorPDUResult obj = new CmsSetMSVCBValuesErrorPDUResult();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetMSVCBValuesErrorPDUResult obj = new CmsSetMSVCBValuesErrorPDUResult();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetMSVCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsSetMSVCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetMSVCBValuesErrorPDUResult obj = new CmsSetMSVCBValuesErrorPDUResult();
        byte[] data = obj.encode("uper");
        CmsSetMSVCBValuesErrorPDUResult d = CmsSetMSVCBValuesErrorPDUResult.decode("uper", data);
        assertEquals(obj, d);
    }
}
