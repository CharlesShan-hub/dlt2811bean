// Auto-generated. Tests for CmsAsdu

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAsduTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAsdu obj = new CmsAsdu();
        assertEquals(0, obj.req_id);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAsdu obj = new CmsAsdu();
        obj.req_id = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAsdu d = MAPPER.readValue(json, CmsAsdu.class);
        assertEquals(obj, d);
    }
}
