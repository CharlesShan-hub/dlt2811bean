// Auto-generated. Tests for CmsOriginator

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsOriginatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsOriginator obj = new CmsOriginator();
        assertEquals(0, obj.or_cat);
        assertNull(obj.or_ident);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsOriginator obj = new CmsOriginator();
        obj.or_cat = 1;
        obj.or_ident = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsOriginator d = MAPPER.readValue(json, CmsOriginator.class);
        assertEquals(obj, d);
    }
}
