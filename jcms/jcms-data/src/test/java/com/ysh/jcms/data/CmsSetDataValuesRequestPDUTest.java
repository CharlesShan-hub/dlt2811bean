// Auto-generated. Tests for CmsSetDataValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetDataValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetDataValuesRequestPDU obj = new CmsSetDataValuesRequestPDU();
        assertNull(obj.data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetDataValuesRequestPDU obj = new CmsSetDataValuesRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetDataValuesRequestPDU d = MAPPER.readValue(json, CmsSetDataValuesRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetDataValuesRequestPDU obj = new CmsSetDataValuesRequestPDU();
        byte[] data = obj.encode("uper");
        CmsSetDataValuesRequestPDU d = CmsSetDataValuesRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
