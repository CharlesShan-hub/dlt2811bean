// Auto-generated. Tests for CmsGetDataSetDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataSetDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataSetDirectoryRequestPDU obj = new CmsGetDataSetDirectoryRequestPDU();
        assertNull(obj.dataset_reference);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataSetDirectoryRequestPDU obj = new CmsGetDataSetDirectoryRequestPDU();
        obj.dataset_reference = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetDataSetDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataSetDirectoryRequestPDU obj = new CmsGetDataSetDirectoryRequestPDU();
        obj.dataset_reference = "test";
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetDataSetDirectoryRequestPDU d = CmsGetDataSetDirectoryRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
