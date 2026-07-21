// Auto-generated. Tests for CmsAnonymousGetDataDirectoryResponsePDUDataAttribute

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetDataDirectoryResponsePDUDataAttributeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetDataDirectoryResponsePDUDataAttribute obj = new CmsAnonymousGetDataDirectoryResponsePDUDataAttribute();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetDataDirectoryResponsePDUDataAttribute obj = new CmsAnonymousGetDataDirectoryResponsePDUDataAttribute();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetDataDirectoryResponsePDUDataAttribute d = MAPPER.readValue(json, CmsAnonymousGetDataDirectoryResponsePDUDataAttribute.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetDataDirectoryResponsePDUDataAttribute obj = new CmsAnonymousGetDataDirectoryResponsePDUDataAttribute();
        obj.reference = "test";
        obj.fc = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousGetDataDirectoryResponsePDUDataAttribute d = CmsAnonymousGetDataDirectoryResponsePDUDataAttribute.decode("uper", data);
        assertEquals(obj, d);
    }
}
