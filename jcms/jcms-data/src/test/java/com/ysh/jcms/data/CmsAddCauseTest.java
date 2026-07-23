// Auto-generated. Tests for CmsAddCause

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAddCauseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAddCause obj = new CmsAddCause();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsAddCause obj = new CmsAddCause(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAddCause obj = new CmsAddCause(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsAddCause d = MAPPER.readValue(json, CmsAddCause.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAddCause obj = new CmsAddCause(1);
        byte[] data = obj.encode("uper");
        CmsAddCause d = CmsAddCause.decode("uper", data);
        assertEquals(obj, d);
    }
}
