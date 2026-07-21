// Auto-generated. Tests for CmsSetGoCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetGoCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetGoCBValuesErrorPDUResult obj = new CmsSetGoCBValuesErrorPDUResult();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetGoCBValuesErrorPDUResult obj = new CmsSetGoCBValuesErrorPDUResult();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetGoCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsSetGoCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetGoCBValuesErrorPDUResult obj = new CmsSetGoCBValuesErrorPDUResult();
        byte[] data = obj.encode("uper");
        CmsSetGoCBValuesErrorPDUResult d = CmsSetGoCBValuesErrorPDUResult.decode("uper", data);
        assertEquals(obj, d);
    }
}
