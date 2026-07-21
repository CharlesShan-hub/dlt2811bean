// Auto-generated. Tests for CmsSetDataSetValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetDataSetValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetDataSetValuesErrorPDU obj = new CmsSetDataSetValuesErrorPDU();
        assertNotNull(obj.result);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetDataSetValuesErrorPDU obj = new CmsSetDataSetValuesErrorPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetDataSetValuesErrorPDU d = MAPPER.readValue(json, CmsSetDataSetValuesErrorPDU.class);
        assertEquals(obj, d);
    }
}
