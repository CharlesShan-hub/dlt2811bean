// Auto-generated. Tests for CmsGetGOOSEElementNumberRequestPDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGOOSEElementNumberRequestPDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGOOSEElementNumberRequestPDUMemberData obj = new CmsGetGOOSEElementNumberRequestPDUMemberData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGOOSEElementNumberRequestPDUMemberData obj = new CmsGetGOOSEElementNumberRequestPDUMemberData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGOOSEElementNumberRequestPDUMemberData d = MAPPER.readValue(json, CmsGetGOOSEElementNumberRequestPDUMemberData.class);
        assertEquals(obj, d);
    }
}
