// Auto-generated. Tests for CmsSetDataValuesRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetDataValuesRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetDataValuesRequestPDUData obj = new CmsSetDataValuesRequestPDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetDataValuesRequestPDUData obj = new CmsSetDataValuesRequestPDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetDataValuesRequestPDUData d = MAPPER.readValue(json, CmsSetDataValuesRequestPDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetDataValuesRequestPDUData obj = new CmsSetDataValuesRequestPDUData();
        byte[] data = obj.encode("uper");
        CmsSetDataValuesRequestPDUData d = CmsSetDataValuesRequestPDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
