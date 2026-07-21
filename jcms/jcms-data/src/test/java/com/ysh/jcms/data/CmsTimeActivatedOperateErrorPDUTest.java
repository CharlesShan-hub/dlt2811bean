// Auto-generated. Tests for CmsTimeActivatedOperateErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsTimeActivatedOperateErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsTimeActivatedOperateErrorPDU obj = new CmsTimeActivatedOperateErrorPDU();
        assertNull(obj.reference);
        assertNull(obj.ctl_val);
        assertNull(obj.oper_tm);
        assertNull(obj.origin);
        assertEquals(0, obj.ctl_num);
        assertNull(obj.t);
        assertFalse(obj.test);
        assertEquals(0, obj.check);
        assertEquals(0, obj.add_cause);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsTimeActivatedOperateErrorPDU obj = new CmsTimeActivatedOperateErrorPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsTimeActivatedOperateErrorPDU d = MAPPER.readValue(json, CmsTimeActivatedOperateErrorPDU.class);
        assertEquals(obj, d);
    }
}
