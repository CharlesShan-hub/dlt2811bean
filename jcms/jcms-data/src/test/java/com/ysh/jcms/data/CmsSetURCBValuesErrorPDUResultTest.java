// Auto-generated. Tests for CmsSetURCBValuesErrorPDUResult

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetURCBValuesErrorPDUResultTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetURCBValuesErrorPDUResult obj = new CmsSetURCBValuesErrorPDUResult();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetURCBValuesErrorPDUResult obj = new CmsSetURCBValuesErrorPDUResult();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetURCBValuesErrorPDUResult d = MAPPER.readValue(json, CmsSetURCBValuesErrorPDUResult.class);
        assertEquals(obj, d);
    }
}
