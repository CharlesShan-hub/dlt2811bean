// Auto-generated. Tests for CmsSetLCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetLCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetLCBValuesErrorPDUResult obj = new CmsSetLCBValuesErrorPDUResult();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetLCBValuesErrorPDUResult obj = new CmsSetLCBValuesErrorPDUResult();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetLCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsSetLCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetLCBValuesErrorPDUResult obj = new CmsSetLCBValuesErrorPDUResult();
        byte[] data = obj.encode("uper");
        CmsSetLCBValuesErrorPDUResult d = CmsSetLCBValuesErrorPDUResult.decode("uper", data);
        assertEquals(obj, d);
    }
}
