// Auto-generated. Tests for CmsGetGoCbValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoCbValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoCbValuesResponsePDU obj = new CmsGetGoCbValuesResponsePDU();
        assertNull(obj.gocb);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoCbValuesResponsePDU obj = new CmsGetGoCbValuesResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoCbValuesResponsePDU d = MAPPER.readValue(json, CmsGetGoCbValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
