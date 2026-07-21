// Auto-generated. Tests for CmsGetDataDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDirectoryResponsePDU obj = new CmsGetDataDirectoryResponsePDU();
        assertNull(obj.data_attribute);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDirectoryResponsePDU obj = new CmsGetDataDirectoryResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetDataDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataDirectoryResponsePDU obj = new CmsGetDataDirectoryResponsePDU();
        obj.more_follows = true;
        byte[] data = obj.encode("uper");
        CmsGetDataDirectoryResponsePDU d = CmsGetDataDirectoryResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
