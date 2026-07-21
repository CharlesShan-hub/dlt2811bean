// Auto-generated. Tests for CmsGetDataSetValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataSetValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataSetValuesRequestPDU obj = new CmsGetDataSetValuesRequestPDU();
        assertNull(obj.dataset_reference);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataSetValuesRequestPDU obj = new CmsGetDataSetValuesRequestPDU();
        obj.dataset_reference = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetValuesRequestPDU d = MAPPER.readValue(json, CmsGetDataSetValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataSetValuesRequestPDU obj = new CmsGetDataSetValuesRequestPDU();
        obj.dataset_reference = "test";
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetDataSetValuesRequestPDU d = CmsGetDataSetValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
