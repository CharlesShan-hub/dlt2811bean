// Auto-generated. Tests for CmsGetEditSGValueResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetEditSGValueResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetEditSGValueResponsePDU obj = new CmsGetEditSGValueResponsePDU();
        assertNotNull(obj.value);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetEditSGValueResponsePDU obj = new CmsGetEditSGValueResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetEditSGValueResponsePDU d = MAPPER.readValue(json, CmsGetEditSGValueResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetEditSGValueResponsePDU obj = new CmsGetEditSGValueResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetEditSGValueResponsePDU d = CmsGetEditSGValueResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
