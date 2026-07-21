// Auto-generated. Tests for CmsGetDataValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataValuesErrorPDU obj = new CmsGetDataValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetDataValuesErrorPDU obj = new CmsGetDataValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataValuesErrorPDU obj = new CmsGetDataValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataValuesErrorPDU d = MAPPER.readValue(json, CmsGetDataValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataValuesErrorPDU obj = new CmsGetDataValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetDataValuesErrorPDU d = CmsGetDataValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
