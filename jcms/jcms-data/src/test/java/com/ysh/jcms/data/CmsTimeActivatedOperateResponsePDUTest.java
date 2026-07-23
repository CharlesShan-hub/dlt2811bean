// Auto-generated. Tests for CmsTimeActivatedOperateResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsTimeActivatedOperateResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsTimeActivatedOperateResponsePDU obj = new CmsTimeActivatedOperateResponsePDU();
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
        CmsTimeActivatedOperateResponsePDU obj = new CmsTimeActivatedOperateResponsePDU();
        obj.reference = "test";
        if (obj.ctl_val == null) obj.ctl_val = new CmsData();
        obj.oper_tm = new byte[]{0x01, 0x02};
        if (obj.origin == null) obj.origin = new CmsOriginator();
        obj.ctl_num = 1;
        obj.t = new byte[]{0x01, 0x02};
        obj.test = true;
        obj.check = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsTimeActivatedOperateResponsePDU d = MAPPER.readValue(json, CmsTimeActivatedOperateResponsePDU.class);
        assertEquals(obj, d);
    }
}
