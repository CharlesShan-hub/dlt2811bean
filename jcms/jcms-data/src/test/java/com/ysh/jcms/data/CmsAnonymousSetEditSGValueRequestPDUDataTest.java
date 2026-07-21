// Auto-generated. Tests for CmsAnonymousSetEditSGValueRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousSetEditSGValueRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousSetEditSGValueRequestPDUData obj = new CmsAnonymousSetEditSGValueRequestPDUData();
        assertNull(obj.reference);
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousSetEditSGValueRequestPDUData obj = new CmsAnonymousSetEditSGValueRequestPDUData();
        obj.reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousSetEditSGValueRequestPDUData d = MAPPER.readValue(json, CmsAnonymousSetEditSGValueRequestPDUData.class);
        assertEquals(obj, d);
    }
}
