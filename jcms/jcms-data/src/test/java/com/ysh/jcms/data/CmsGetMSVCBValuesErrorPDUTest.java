// Auto-generated. Tests for CmsGetMSVCBValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetMSVCBValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetMSVCBValuesErrorPDU obj = new CmsGetMSVCBValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetMSVCBValuesErrorPDU obj = new CmsGetMSVCBValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetMSVCBValuesErrorPDU obj = new CmsGetMSVCBValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetMSVCBValuesErrorPDU d = MAPPER.readValue(json, CmsGetMSVCBValuesErrorPDU.class);
        assertEquals(obj, d);
    }
}
