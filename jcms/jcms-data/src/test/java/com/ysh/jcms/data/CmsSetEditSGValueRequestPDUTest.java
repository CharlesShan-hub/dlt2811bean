// Auto-generated. Tests for CmsSetEditSGValueRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetEditSGValueRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetEditSGValueRequestPDU obj = new CmsSetEditSGValueRequestPDU();
        assertNull(obj.data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetEditSGValueRequestPDU obj = new CmsSetEditSGValueRequestPDU();
        obj.data = java.util.Collections.singletonList(new CmsAnonymousSetEditSGValueRequestPDUData());
        String json = MAPPER.writeValueAsString(obj);
        CmsSetEditSGValueRequestPDU d = MAPPER.readValue(json, CmsSetEditSGValueRequestPDU.class);
        assertEquals(obj, d);
    }
}
