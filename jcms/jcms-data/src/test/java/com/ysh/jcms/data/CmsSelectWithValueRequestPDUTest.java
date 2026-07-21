// Auto-generated. Tests for CmsSelectWithValueRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectWithValueRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectWithValueRequestPDU obj = new CmsSelectWithValueRequestPDU();
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
        CmsSelectWithValueRequestPDU obj = new CmsSelectWithValueRequestPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectWithValueRequestPDU d = MAPPER.readValue(json, CmsSelectWithValueRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSelectWithValueRequestPDU obj = new CmsSelectWithValueRequestPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        byte[] data = obj.encode("uper");
        CmsSelectWithValueRequestPDU d = CmsSelectWithValueRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
