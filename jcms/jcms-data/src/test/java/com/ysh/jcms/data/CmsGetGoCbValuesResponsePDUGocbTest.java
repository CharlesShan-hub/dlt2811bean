// Auto-generated. Tests for CmsGetGoCbValuesResponsePDUGocb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoCbValuesResponsePDUGocbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoCbValuesResponsePDUGocb obj = new CmsGetGoCbValuesResponsePDUGocb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoCbValuesResponsePDUGocb obj = new CmsGetGoCbValuesResponsePDUGocb();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoCbValuesResponsePDUGocb d = MAPPER.readValue(json, CmsGetGoCbValuesResponsePDUGocb.class);
        assertEquals(obj, d);
    }
}
