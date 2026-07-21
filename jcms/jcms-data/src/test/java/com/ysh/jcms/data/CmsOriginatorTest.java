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
        obj.or_cat = 42;
        obj.or_ident = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsOriginator d = MAPPER.readValue(json, CmsOriginator.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsOriginator obj = new CmsOriginator();
        obj.or_cat = 42;
        obj.or_ident = new byte[0];
        byte[] data = obj.encode("uper");
        CmsOriginator d = CmsOriginator.decode("uper", data);
        assertEquals(obj, d);
    }
}
