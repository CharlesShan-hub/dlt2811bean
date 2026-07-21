// Auto-generated. Tests for CmsGetLogStatusValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogStatusValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogStatusValuesRequestPDU obj = new CmsGetLogStatusValuesRequestPDU();
        assertNotNull(obj.log_reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogStatusValuesRequestPDU obj = new CmsGetLogStatusValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogStatusValuesRequestPDU d = MAPPER.readValue(json, CmsGetLogStatusValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLogStatusValuesRequestPDU obj = new CmsGetLogStatusValuesRequestPDU();
        byte[] data = obj.encode("uper");
        CmsGetLogStatusValuesRequestPDU d = CmsGetLogStatusValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
