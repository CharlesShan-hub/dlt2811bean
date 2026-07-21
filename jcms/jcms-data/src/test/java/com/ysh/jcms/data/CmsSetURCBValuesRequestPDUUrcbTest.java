// Auto-generated. Tests for CmsSetURCBValuesRequestPDUUrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetURCBValuesRequestPDUUrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetURCBValuesRequestPDUUrcb obj = new CmsSetURCBValuesRequestPDUUrcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetURCBValuesRequestPDUUrcb obj = new CmsSetURCBValuesRequestPDUUrcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetURCBValuesRequestPDUUrcb d = MAPPER.readValue(json, CmsSetURCBValuesRequestPDUUrcb.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsSetURCBValuesRequestPDUUrcb obj = new CmsSetURCBValuesRequestPDUUrcb();
        byte[] data = obj.encode("uper");
        CmsSetURCBValuesRequestPDUUrcb d = CmsSetURCBValuesRequestPDUUrcb.decode("uper", data);
        assertEquals(obj, d);
    }
}
