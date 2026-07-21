// Auto-generated. Tests for CmsCheck

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCheckTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCheck obj = new CmsCheck();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsCheck obj = new CmsCheck(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCheck obj = new CmsCheck(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsCheck d = MAPPER.readValue(json, CmsCheck.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsCheck obj = new CmsCheck(42);
        byte[] data = obj.encode("uper");
        CmsCheck d = CmsCheck.decode("uper", data);
        assertEquals(obj, d);
    }
}
