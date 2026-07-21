// Auto-generated. Tests for CmsGetAllDataValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataValuesRequestPDU obj = new CmsGetAllDataValuesRequestPDU();
        assertNull(obj.reference);
        assertNull(obj.fc);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataValuesRequestPDU obj = new CmsGetAllDataValuesRequestPDU();
        obj.fc = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataValuesRequestPDU d = MAPPER.readValue(json, CmsGetAllDataValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllDataValuesRequestPDU obj = new CmsGetAllDataValuesRequestPDU();
        obj.fc = "test";
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetAllDataValuesRequestPDU d = CmsGetAllDataValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
