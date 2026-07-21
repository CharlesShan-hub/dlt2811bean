// Auto-generated. Tests for CmsGetEditSGValueErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetEditSGValueErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetEditSGValueErrorPDU obj = new CmsGetEditSGValueErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetEditSGValueErrorPDU obj = new CmsGetEditSGValueErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetEditSGValueErrorPDU obj = new CmsGetEditSGValueErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetEditSGValueErrorPDU d = MAPPER.readValue(json, CmsGetEditSGValueErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetEditSGValueErrorPDU obj = new CmsGetEditSGValueErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetEditSGValueErrorPDU d = CmsGetEditSGValueErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
