// Auto-generated. Tests for CmsGetDataValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataValuesRequestPDU obj = new CmsGetDataValuesRequestPDU();
        assertNull(obj.data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataValuesRequestPDU obj = new CmsGetDataValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataValuesRequestPDU d = MAPPER.readValue(json, CmsGetDataValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataValuesRequestPDU obj = new CmsGetDataValuesRequestPDU();
        byte[] data = obj.encode("uper");
        CmsGetDataValuesRequestPDU d = CmsGetDataValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
