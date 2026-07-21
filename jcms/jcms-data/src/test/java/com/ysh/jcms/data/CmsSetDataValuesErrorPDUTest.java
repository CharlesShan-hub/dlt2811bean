// Auto-generated. Tests for CmsSetDataValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetDataValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetDataValuesErrorPDU obj = new CmsSetDataValuesErrorPDU();
        assertNotNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetDataValuesErrorPDU obj = new CmsSetDataValuesErrorPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetDataValuesErrorPDU d = MAPPER.readValue(json, CmsSetDataValuesErrorPDU.class);
        assertEquals(obj, d);
    }
}
