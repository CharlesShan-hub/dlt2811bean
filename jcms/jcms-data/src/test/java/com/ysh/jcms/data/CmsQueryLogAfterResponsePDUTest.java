// Auto-generated. Tests for CmsQueryLogAfterResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsQueryLogAfterResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsQueryLogAfterResponsePDU obj = new CmsQueryLogAfterResponsePDU();
        assertNotNull(obj.log_entry);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsQueryLogAfterResponsePDU obj = new CmsQueryLogAfterResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsQueryLogAfterResponsePDU d = MAPPER.readValue(json, CmsQueryLogAfterResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsQueryLogAfterResponsePDU obj = new CmsQueryLogAfterResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsQueryLogAfterResponsePDU d = CmsQueryLogAfterResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
