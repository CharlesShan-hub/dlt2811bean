// Auto-generated. Tests for CmsDbpos

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDbposTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDbpos obj = new CmsDbpos();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsDbpos obj = new CmsDbpos(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDbpos obj = new CmsDbpos(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsDbpos d = MAPPER.readValue(json, CmsDbpos.class);
        assertEquals(obj, d);
    }
}
