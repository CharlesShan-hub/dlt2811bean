// Auto-generated. Tests for CmsAnonymousGetGoReferenceResponsePDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetGoReferenceResponsePDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetGoReferenceResponsePDUMemberData obj = new CmsAnonymousGetGoReferenceResponsePDUMemberData();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetGoReferenceResponsePDUMemberData obj = new CmsAnonymousGetGoReferenceResponsePDUMemberData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetGoReferenceResponsePDUMemberData d = MAPPER.readValue(json, CmsAnonymousGetGoReferenceResponsePDUMemberData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetGoReferenceResponsePDUMemberData obj = new CmsAnonymousGetGoReferenceResponsePDUMemberData();
        obj.reference = "test";
        obj.fc = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousGetGoReferenceResponsePDUMemberData d = CmsAnonymousGetGoReferenceResponsePDUMemberData.decode("uper", data);
        assertEquals(obj, d);
    }
}
