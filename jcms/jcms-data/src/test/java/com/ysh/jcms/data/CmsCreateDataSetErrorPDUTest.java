// Auto-generated. Tests for CmsCreateDataSetErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCreateDataSetErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCreateDataSetErrorPDU obj = new CmsCreateDataSetErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsCreateDataSetErrorPDU obj = new CmsCreateDataSetErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCreateDataSetErrorPDU obj = new CmsCreateDataSetErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsCreateDataSetErrorPDU d = MAPPER.readValue(json, CmsCreateDataSetErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsCreateDataSetErrorPDU obj = new CmsCreateDataSetErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsCreateDataSetErrorPDU d = CmsCreateDataSetErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
