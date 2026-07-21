// Auto-generated. Tests for CmsGetFileDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetFileDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetFileDirectoryErrorPDU obj = new CmsGetFileDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetFileDirectoryErrorPDU obj = new CmsGetFileDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetFileDirectoryErrorPDU obj = new CmsGetFileDirectoryErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetFileDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetFileDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetFileDirectoryErrorPDU obj = new CmsGetFileDirectoryErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetFileDirectoryErrorPDU d = CmsGetFileDirectoryErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
