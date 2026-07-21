// Auto-generated. Tests for CmsTimeActivatedOperateRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsTimeActivatedOperateRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsTimeActivatedOperateRequestPDU obj = new CmsTimeActivatedOperateRequestPDU();
        assertNull(obj.reference);
        assertNull(obj.ctl_val);
        assertNull(obj.oper_tm);
        assertNull(obj.origin);
        assertEquals(0, obj.ctl_num);
        assertNull(obj.t);
        assertFalse(obj.test);
        assertEquals(0, obj.check);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsTimeActivatedOperateRequestPDU obj = new CmsTimeActivatedOperateRequestPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsTimeActivatedOperateRequestPDU d = MAPPER.readValue(json, CmsTimeActivatedOperateRequestPDU.class);
        assertEquals(obj, d);
    }
}
