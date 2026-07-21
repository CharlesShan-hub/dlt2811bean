// Auto-generated. Tests for CmsSetBRCBValuesRequestPDUBrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetBRCBValuesRequestPDUBrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetBRCBValuesRequestPDUBrcb obj = new CmsSetBRCBValuesRequestPDUBrcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetBRCBValuesRequestPDUBrcb obj = new CmsSetBRCBValuesRequestPDUBrcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetBRCBValuesRequestPDUBrcb d = MAPPER.readValue(json, CmsSetBRCBValuesRequestPDUBrcb.class);
        assertEquals(obj, d);
    }
}
