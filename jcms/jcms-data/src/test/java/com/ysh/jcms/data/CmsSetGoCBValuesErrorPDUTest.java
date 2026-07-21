// Auto-generated. Tests for CmsSetGoCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetGoCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetGoCBValuesErrorPDU obj = new CmsSetGoCBValuesErrorPDU();
        assertNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetGoCBValuesErrorPDU obj = new CmsSetGoCBValuesErrorPDU();
        obj.result = java.util.Collections.singletonList(new CmsAnonymousSetGoCBValuesErrorPDUResult());
        String json = MAPPER.writeValueAsString(obj);
        CmsSetGoCBValuesErrorPDU d = MAPPER.readValue(json, CmsSetGoCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
}
