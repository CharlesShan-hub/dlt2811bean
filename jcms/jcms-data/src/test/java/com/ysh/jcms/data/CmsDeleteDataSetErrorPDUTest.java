// Auto-generated. Tests for CmsDeleteDataSetErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDeleteDataSetErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDeleteDataSetErrorPDU obj = new CmsDeleteDataSetErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsDeleteDataSetErrorPDU obj = new CmsDeleteDataSetErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDeleteDataSetErrorPDU obj = new CmsDeleteDataSetErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsDeleteDataSetErrorPDU d = MAPPER.readValue(json, CmsDeleteDataSetErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsDeleteDataSetErrorPDU obj = new CmsDeleteDataSetErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsDeleteDataSetErrorPDU d = CmsDeleteDataSetErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
