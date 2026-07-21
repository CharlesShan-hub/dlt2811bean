// Auto-generated. Tests for CmsSelectWithValueErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectWithValueErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectWithValueErrorPDU obj = new CmsSelectWithValueErrorPDU();
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
        CmsSelectWithValueErrorPDU obj = new CmsSelectWithValueErrorPDU();
        obj.reference = "test";
        obj.oper_tm = new byte[0];
        obj.ctl_num = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectWithValueErrorPDU d = MAPPER.readValue(json, CmsSelectWithValueErrorPDU.class);
        assertEquals(obj, d);
    }
}
