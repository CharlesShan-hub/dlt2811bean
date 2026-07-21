// Auto-generated. Tests for CmsSelectActiveSGErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectActiveSGErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectActiveSGErrorPDU obj = new CmsSelectActiveSGErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSelectActiveSGErrorPDU obj = new CmsSelectActiveSGErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectActiveSGErrorPDU obj = new CmsSelectActiveSGErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectActiveSGErrorPDU d = MAPPER.readValue(json, CmsSelectActiveSGErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSelectActiveSGErrorPDU obj = new CmsSelectActiveSGErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsSelectActiveSGErrorPDU d = CmsSelectActiveSGErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
