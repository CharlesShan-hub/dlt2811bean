// Auto-generated. Tests for CmsOperateErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsOperateErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsOperateErrorPDU obj = new CmsOperateErrorPDU();
        assertNull(obj.reference);
        assertNull(obj.ctl_val);
        assertNull(obj.origin);
        assertEquals(0, obj.ctl_num);
        assertNull(obj.t);
        assertFalse(obj.test);
        assertEquals(0, obj.check);
        assertEquals(0, obj.add_cause);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsOperateErrorPDU obj = new CmsOperateErrorPDU();
        obj.reference = "test";
        obj.ctl_num = 42;
        obj.t = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsOperateErrorPDU d = MAPPER.readValue(json, CmsOperateErrorPDU.class);
        assertEquals(obj, d);
    }
}
