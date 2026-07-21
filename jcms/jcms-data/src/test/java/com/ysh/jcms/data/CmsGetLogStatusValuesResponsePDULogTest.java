// Auto-generated. Tests for CmsGetLogStatusValuesResponsePDULog

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogStatusValuesResponsePDULogTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogStatusValuesResponsePDULog obj = new CmsGetLogStatusValuesResponsePDULog();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogStatusValuesResponsePDULog obj = new CmsGetLogStatusValuesResponsePDULog();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogStatusValuesResponsePDULog d = MAPPER.readValue(json, CmsGetLogStatusValuesResponsePDULog.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLogStatusValuesResponsePDULog obj = new CmsGetLogStatusValuesResponsePDULog();
        byte[] data = obj.encode("uper");
        CmsGetLogStatusValuesResponsePDULog d = CmsGetLogStatusValuesResponsePDULog.decode("uper", data);
        assertEquals(obj, d);
    }
}
