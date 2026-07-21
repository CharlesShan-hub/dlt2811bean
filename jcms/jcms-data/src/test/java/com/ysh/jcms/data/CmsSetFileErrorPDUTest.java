// Auto-generated. Tests for CmsSetFileErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetFileErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetFileErrorPDU obj = new CmsSetFileErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetFileErrorPDU obj = new CmsSetFileErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetFileErrorPDU obj = new CmsSetFileErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsSetFileErrorPDU d = MAPPER.readValue(json, CmsSetFileErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetFileErrorPDU obj = new CmsSetFileErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsSetFileErrorPDU d = CmsSetFileErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
