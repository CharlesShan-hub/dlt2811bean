// Auto-generated. Tests for CmsControlCode

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsControlCodeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsControlCode obj = new CmsControlCode();
        assertFalse(obj.next);
        assertFalse(obj.resp);
        assertFalse(obj.err);
        assertEquals(0, obj.pi);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsControlCode obj = new CmsControlCode();
        obj.next = true;
        obj.resp = true;
        obj.err = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsControlCode d = MAPPER.readValue(json, CmsControlCode.class);
        assertEquals(obj, d);
    }
}
