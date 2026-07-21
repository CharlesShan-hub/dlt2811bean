// Auto-generated. Tests for CmsGetDataDirectoryResponsePDUDataAttribute

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDirectoryResponsePDUDataAttributeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDirectoryResponsePDUDataAttribute obj = new CmsGetDataDirectoryResponsePDUDataAttribute();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDirectoryResponsePDUDataAttribute obj = new CmsGetDataDirectoryResponsePDUDataAttribute();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDirectoryResponsePDUDataAttribute d = MAPPER.readValue(json, CmsGetDataDirectoryResponsePDUDataAttribute.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataDirectoryResponsePDUDataAttribute obj = new CmsGetDataDirectoryResponsePDUDataAttribute();
        byte[] data = obj.encode("uper");
        CmsGetDataDirectoryResponsePDUDataAttribute d = CmsGetDataDirectoryResponsePDUDataAttribute.decode("uper", data);
        assertEquals(obj, d);
    }
}
