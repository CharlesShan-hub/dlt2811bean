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
        if (obj.ctl_val == null) obj.ctl_val = new CmsData();
        if (obj.origin == null) obj.origin = new CmsOriginator();
        obj.ctl_num = 1;
        obj.t = new byte[]{0x01, 0x02};
        obj.test = true;
        obj.check = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsOperateRequestPDU d = MAPPER.readValue(json, CmsOperateRequestPDU.class);
        assertEquals(obj, d);
    }
}
