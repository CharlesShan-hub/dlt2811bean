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
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsTimeActivatedOperateResponsePDU d = MAPPER.readValue(json, CmsTimeActivatedOperateResponsePDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsTimeActivatedOperateResponsePDU obj = new CmsTimeActivatedOperateResponsePDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        byte[] data = obj.encode("uper");
        CmsTimeActivatedOperateResponsePDU d = CmsTimeActivatedOperateResponsePDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
