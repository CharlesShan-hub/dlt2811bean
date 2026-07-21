// Auto-generated. Tests for CmsGetDataSetDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataSetDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataSetDirectoryResponsePDU obj = new CmsGetDataSetDirectoryResponsePDU();
        assertNull(obj.member_data);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataSetDirectoryResponsePDU obj = new CmsGetDataSetDirectoryResponsePDU();
        obj.member_data = java.util.Collections.singletonList(new CmsAnonymousGetDataSetDirectoryResponsePDUMemberData());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetDataSetDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
}
