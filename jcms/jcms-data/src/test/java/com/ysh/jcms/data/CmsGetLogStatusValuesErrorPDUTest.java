// Auto-generated. Tests for CmsGetLogStatusValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogStatusValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogStatusValuesErrorPDU obj = new CmsGetLogStatusValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetLogStatusValuesErrorPDU obj = new CmsGetLogStatusValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogStatusValuesErrorPDU obj = new CmsGetLogStatusValuesErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogStatusValuesErrorPDU d = MAPPER.readValue(json, CmsGetLogStatusValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLogStatusValuesErrorPDU obj = new CmsGetLogStatusValuesErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetLogStatusValuesErrorPDU d = CmsGetLogStatusValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
