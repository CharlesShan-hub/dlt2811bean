// Auto-generated. Tests for CmsGetDataSetDirectoryResponsePDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataSetDirectoryResponsePDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataSetDirectoryResponsePDUMemberData obj = new CmsGetDataSetDirectoryResponsePDUMemberData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataSetDirectoryResponsePDUMemberData obj = new CmsGetDataSetDirectoryResponsePDUMemberData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetDirectoryResponsePDUMemberData d = MAPPER.readValue(json, CmsGetDataSetDirectoryResponsePDUMemberData.class);
        assertEquals(obj, d);
    }
}
