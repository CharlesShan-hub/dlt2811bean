// Auto-generated. Tests for CmsQueryLogAfterRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsQueryLogAfterRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsQueryLogAfterRequestPDU obj = new CmsQueryLogAfterRequestPDU();
        assertNull(obj.log_reference);
        assertNull(obj.start_time);
        assertNull(obj.entry);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsQueryLogAfterRequestPDU obj = new CmsQueryLogAfterRequestPDU();
        obj.log_reference = "test";
        obj.start_time = new byte[0];
        obj.entry = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsQueryLogAfterRequestPDU d = MAPPER.readValue(json, CmsQueryLogAfterRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsQueryLogAfterRequestPDU obj = new CmsQueryLogAfterRequestPDU();
        obj.log_reference = "test";
        obj.start_time = new byte[0];
        obj.entry = new byte[0];
        byte[] data = obj.encode("uper");
        CmsQueryLogAfterRequestPDU d = CmsQueryLogAfterRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
