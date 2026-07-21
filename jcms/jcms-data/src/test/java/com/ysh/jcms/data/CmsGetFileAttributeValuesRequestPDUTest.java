// Auto-generated. Tests for CmsGetFileAttributeValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileAttributeValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileAttributeValuesRequestPDU obj = new CmsGetFileAttributeValuesRequestPDU();
        assertNull(obj.filename);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileAttributeValuesRequestPDU obj = new CmsGetFileAttributeValuesRequestPDU();
        obj.filename = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileAttributeValuesRequestPDU d = MAPPER.readValue(json, CmsGetFileAttributeValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
