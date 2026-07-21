// Auto-generated. Tests for CmsSelectErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectErrorPDU obj = new CmsSelectErrorPDU();
        assertNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectErrorPDU obj = new CmsSelectErrorPDU();
        obj.reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectErrorPDU d = MAPPER.readValue(json, CmsSelectErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSelectErrorPDU obj = new CmsSelectErrorPDU();
        obj.reference = "test";
        byte[] data = obj.encode("uper");
        CmsSelectErrorPDU d = CmsSelectErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
