// Auto-generated. Tests for CmsOperateResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsOperateResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsOperateResponsePDU obj = new CmsOperateResponsePDU();
        assertNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsOperateResponsePDU obj = new CmsOperateResponsePDU();
        obj.reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsOperateResponsePDU d = MAPPER.readValue(json, CmsOperateResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsOperateResponsePDU obj = new CmsOperateResponsePDU();
        obj.reference = "test";
        byte[] data = obj.encode("uper");
        CmsOperateResponsePDU d = CmsOperateResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
