// Auto-generated. Tests for CmsSetDataValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetDataValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetDataValuesResponsePDU obj = new CmsSetDataValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetDataValuesResponsePDU obj = new CmsSetDataValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetDataValuesResponsePDU obj = new CmsSetDataValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetDataValuesResponsePDU d = MAPPER.readValue(json, CmsSetDataValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
