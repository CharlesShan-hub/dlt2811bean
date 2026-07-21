// Auto-generated. Tests for CmsCreateDataSetRequestPDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCreateDataSetRequestPDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCreateDataSetRequestPDUMemberData obj = new CmsCreateDataSetRequestPDUMemberData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCreateDataSetRequestPDUMemberData obj = new CmsCreateDataSetRequestPDUMemberData();
        String json = MAPPER.writeValueAsString(obj);
        CmsCreateDataSetRequestPDUMemberData d = MAPPER.readValue(json, CmsCreateDataSetRequestPDUMemberData.class);
        assertEquals(obj, d);
    }
}
