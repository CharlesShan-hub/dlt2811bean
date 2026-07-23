// Auto-generated. Tests for CmsGetDataSetValuesErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataSetValuesErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataSetValuesErrorPDU obj = new CmsGetDataSetValuesErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetDataSetValuesErrorPDU obj = new CmsGetDataSetValuesErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataSetValuesErrorPDU obj = new CmsGetDataSetValuesErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetValuesErrorPDU d = MAPPER.readValue(json, CmsGetDataSetValuesErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataSetValuesErrorPDU obj = new CmsGetDataSetValuesErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetDataSetValuesErrorPDU d = CmsGetDataSetValuesErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
