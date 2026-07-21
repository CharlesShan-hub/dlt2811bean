// Auto-generated. Tests for CmsApch

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsApchTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsApch obj = new CmsApch();
        assertNull(obj.cc);
        assertEquals(0, obj.sc);
        assertEquals(0, obj.fl);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsApch obj = new CmsApch();
        obj.sc = 42;
        obj.fl = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsApch d = MAPPER.readValue(json, CmsApch.class);
        assertEquals(obj, d);
    }
}
