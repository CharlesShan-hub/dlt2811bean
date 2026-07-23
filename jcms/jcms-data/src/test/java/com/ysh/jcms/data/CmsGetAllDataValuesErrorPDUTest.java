// Auto-generated. Tests for CmsGetAllDataValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataValuesErrorPDU obj = new CmsGetAllDataValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetAllDataValuesErrorPDU obj = new CmsGetAllDataValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataValuesErrorPDU obj = new CmsGetAllDataValuesErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataValuesErrorPDU d = MAPPER.readValue(json, CmsGetAllDataValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllDataValuesErrorPDU obj = new CmsGetAllDataValuesErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetAllDataValuesErrorPDU d = CmsGetAllDataValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
