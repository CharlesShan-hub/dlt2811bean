// Auto-generated. Tests for CmsGetDataValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataValuesResponsePDU obj = new CmsGetDataValuesResponsePDU();
        assertNotNull(obj.value);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataValuesResponsePDU obj = new CmsGetDataValuesResponsePDU();
        obj.value = java.util.Collections.singletonList(new CmsData());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataValuesResponsePDU d = MAPPER.readValue(json, CmsGetDataValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
