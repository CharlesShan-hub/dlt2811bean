// Auto-generated. Tests for CmsGetGoCbValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoCbValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoCbValuesRequestPDU obj = new CmsGetGoCbValuesRequestPDU();
        assertNotNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoCbValuesRequestPDU obj = new CmsGetGoCbValuesRequestPDU();
        obj.reference = java.util.Collections.singletonList("test");
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoCbValuesRequestPDU d = MAPPER.readValue(json, CmsGetGoCbValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
