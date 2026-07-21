// Auto-generated. Tests for CmsAnonymousGetDataSetDirectoryResponsePDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetDataSetDirectoryResponsePDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetDataSetDirectoryResponsePDUMemberData obj = new CmsAnonymousGetDataSetDirectoryResponsePDUMemberData();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetDataSetDirectoryResponsePDUMemberData obj = new CmsAnonymousGetDataSetDirectoryResponsePDUMemberData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetDataSetDirectoryResponsePDUMemberData d = MAPPER.readValue(json, CmsAnonymousGetDataSetDirectoryResponsePDUMemberData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetDataSetDirectoryResponsePDUMemberData obj = new CmsAnonymousGetDataSetDirectoryResponsePDUMemberData();
        obj.reference = "test";
        obj.fc = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousGetDataSetDirectoryResponsePDUMemberData d = CmsAnonymousGetDataSetDirectoryResponsePDUMemberData.decode("uper", data);
        assertEquals(obj, d);
    }
}
