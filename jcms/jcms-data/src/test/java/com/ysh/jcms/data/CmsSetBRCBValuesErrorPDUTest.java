// Auto-generated. Tests for CmsSetBRCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetBRCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetBRCBValuesErrorPDU obj = new CmsSetBRCBValuesErrorPDU();
        assertNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetBRCBValuesErrorPDU obj = new CmsSetBRCBValuesErrorPDU();
        obj.result = java.util.Collections.singletonList(new CmsAnonymousSetBRCBValuesErrorPDUResult());
        String json = MAPPER.writeValueAsString(obj);
        CmsSetBRCBValuesErrorPDU d = MAPPER.readValue(json, CmsSetBRCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
}
