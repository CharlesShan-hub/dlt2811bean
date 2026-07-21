// Auto-generated. Tests for CmsGetServerDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetServerDirectoryErrorPDU obj = new CmsGetServerDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetServerDirectoryErrorPDU obj = new CmsGetServerDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetServerDirectoryErrorPDU obj = new CmsGetServerDirectoryErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetServerDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetServerDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetServerDirectoryErrorPDU obj = new CmsGetServerDirectoryErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetServerDirectoryErrorPDU d = CmsGetServerDirectoryErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
