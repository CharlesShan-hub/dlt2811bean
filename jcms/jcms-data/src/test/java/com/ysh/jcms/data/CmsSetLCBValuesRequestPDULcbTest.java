// Auto-generated. Tests for CmsSetLCBValuesRequestPDULcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetLCBValuesRequestPDULcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetLCBValuesRequestPDULcb obj = new CmsSetLCBValuesRequestPDULcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetLCBValuesRequestPDULcb obj = new CmsSetLCBValuesRequestPDULcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetLCBValuesRequestPDULcb d = MAPPER.readValue(json, CmsSetLCBValuesRequestPDULcb.class);
        assertEquals(obj, d);
    }
}
