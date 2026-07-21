// Auto-generated. Tests for CmsGetGoReferenceResponsePDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoReferenceResponsePDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoReferenceResponsePDUMemberData obj = new CmsGetGoReferenceResponsePDUMemberData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoReferenceResponsePDUMemberData obj = new CmsGetGoReferenceResponsePDUMemberData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoReferenceResponsePDUMemberData d = MAPPER.readValue(json, CmsGetGoReferenceResponsePDUMemberData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetGoReferenceResponsePDUMemberData obj = new CmsGetGoReferenceResponsePDUMemberData();
        byte[] data = obj.encode("uper");
        CmsGetGoReferenceResponsePDUMemberData d = CmsGetGoReferenceResponsePDUMemberData.decode("uper", data);
        assertEquals(obj, d);
    }
}
