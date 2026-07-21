// Auto-generated. Tests for CmsGetGOOSEElementNumberErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGOOSEElementNumberErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGOOSEElementNumberErrorPDU obj = new CmsGetGOOSEElementNumberErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetGOOSEElementNumberErrorPDU obj = new CmsGetGOOSEElementNumberErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGOOSEElementNumberErrorPDU obj = new CmsGetGOOSEElementNumberErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGOOSEElementNumberErrorPDU d = MAPPER.readValue(json, CmsGetGOOSEElementNumberErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetGOOSEElementNumberErrorPDU obj = new CmsGetGOOSEElementNumberErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetGOOSEElementNumberErrorPDU d = CmsGetGOOSEElementNumberErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
