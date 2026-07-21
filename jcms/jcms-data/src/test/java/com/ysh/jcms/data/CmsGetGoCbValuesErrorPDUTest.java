// Auto-generated. Tests for CmsGetGoCbValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoCbValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoCbValuesErrorPDU obj = new CmsGetGoCbValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetGoCbValuesErrorPDU obj = new CmsGetGoCbValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoCbValuesErrorPDU obj = new CmsGetGoCbValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoCbValuesErrorPDU d = MAPPER.readValue(json, CmsGetGoCbValuesErrorPDU.class);
        assertEquals(obj, d);
    }
}
