// Auto-generated. Tests for CmsAnonymousGetEditSGValueRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetEditSGValueRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetEditSGValueRequestPDUData obj = new CmsAnonymousGetEditSGValueRequestPDUData();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetEditSGValueRequestPDUData obj = new CmsAnonymousGetEditSGValueRequestPDUData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetEditSGValueRequestPDUData d = MAPPER.readValue(json, CmsAnonymousGetEditSGValueRequestPDUData.class);
        assertEquals(obj, d);
    }
}
