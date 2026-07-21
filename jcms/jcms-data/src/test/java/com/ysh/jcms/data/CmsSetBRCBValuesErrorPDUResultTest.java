// Auto-generated. Tests for CmsSetBRCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetBRCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetBRCBValuesErrorPDUResult obj = new CmsSetBRCBValuesErrorPDUResult();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetBRCBValuesErrorPDUResult obj = new CmsSetBRCBValuesErrorPDUResult();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetBRCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsSetBRCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetBRCBValuesErrorPDUResult obj = new CmsSetBRCBValuesErrorPDUResult();
        byte[] data = obj.encode("uper");
        CmsSetBRCBValuesErrorPDUResult d = CmsSetBRCBValuesErrorPDUResult.decode("uper", data);
        assertEquals(obj, d);
    }
}
