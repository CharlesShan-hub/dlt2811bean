// Auto-generated. Tests for CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetGOOSEElementNumberRequestPDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData obj = new CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData obj = new CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData d = MAPPER.readValue(json, CmsAnonymousGetGOOSEElementNumberRequestPDUMemberData.class);
        assertEquals(obj, d);
    }
}
