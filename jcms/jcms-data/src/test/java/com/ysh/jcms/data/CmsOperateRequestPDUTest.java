// Auto-generated. Tests for CmsOperateRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsOperateRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsOperateRequestPDU obj = new CmsOperateRequestPDU();
        assertNull(obj.reference);
        assertNull(obj.ctl_val);
        assertNull(obj.origin);
        assertEquals(0, obj.ctl_num);
        assertNull(obj.t);
        assertFalse(obj.test);
        assertEquals(0, obj.check);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsOperateRequestPDU obj = new CmsOperateRequestPDU();
        obj.reference = "test";
        obj.ctl_num = 42;
        obj.t = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsOperateRequestPDU d = MAPPER.readValue(json, CmsOperateRequestPDU.class);
        assertEquals(obj, d);
    }
}
