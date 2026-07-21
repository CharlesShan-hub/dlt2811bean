// Auto-generated. Tests for CmsGetDataDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDirectoryErrorPDU obj = new CmsGetDataDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetDataDirectoryErrorPDU obj = new CmsGetDataDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDirectoryErrorPDU obj = new CmsGetDataDirectoryErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetDataDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataDirectoryErrorPDU obj = new CmsGetDataDirectoryErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetDataDirectoryErrorPDU d = CmsGetDataDirectoryErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
