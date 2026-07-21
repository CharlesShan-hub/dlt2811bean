// Auto-generated. Tests for CmsAnonymousCreateDataSetRequestPDUMemberData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousCreateDataSetRequestPDUMemberDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousCreateDataSetRequestPDUMemberData obj = new CmsAnonymousCreateDataSetRequestPDUMemberData();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousCreateDataSetRequestPDUMemberData obj = new CmsAnonymousCreateDataSetRequestPDUMemberData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousCreateDataSetRequestPDUMemberData d = MAPPER.readValue(json, CmsAnonymousCreateDataSetRequestPDUMemberData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousCreateDataSetRequestPDUMemberData obj = new CmsAnonymousCreateDataSetRequestPDUMemberData();
        obj.reference = "test";
        obj.fc = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousCreateDataSetRequestPDUMemberData d = CmsAnonymousCreateDataSetRequestPDUMemberData.decode("uper", data);
        assertEquals(obj, d);
    }
}
