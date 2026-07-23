// Auto-generated. Tests for CmsDeleteFileErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDeleteFileErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDeleteFileErrorPDU obj = new CmsDeleteFileErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsDeleteFileErrorPDU obj = new CmsDeleteFileErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDeleteFileErrorPDU obj = new CmsDeleteFileErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsDeleteFileErrorPDU d = MAPPER.readValue(json, CmsDeleteFileErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsDeleteFileErrorPDU obj = new CmsDeleteFileErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsDeleteFileErrorPDU d = CmsDeleteFileErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
