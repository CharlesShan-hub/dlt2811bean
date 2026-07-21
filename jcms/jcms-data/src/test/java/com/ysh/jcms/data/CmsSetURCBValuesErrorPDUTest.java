// Auto-generated. Tests for CmsSetURCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetURCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetURCBValuesErrorPDU obj = new CmsSetURCBValuesErrorPDU();
        assertNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetURCBValuesErrorPDU obj = new CmsSetURCBValuesErrorPDU();
        obj.result = java.util.Collections.singletonList(new CmsAnonymousSetURCBValuesErrorPDUResult());
        String json = MAPPER.writeValueAsString(obj);
        CmsSetURCBValuesErrorPDU d = MAPPER.readValue(json, CmsSetURCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
}
