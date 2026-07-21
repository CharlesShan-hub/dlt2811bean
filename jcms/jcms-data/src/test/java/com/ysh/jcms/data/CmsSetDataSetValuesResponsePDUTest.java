// Auto-generated. Tests for CmsSetDataSetValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetDataSetValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetDataSetValuesResponsePDU obj = new CmsSetDataSetValuesResponsePDU();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsSetDataSetValuesResponsePDU obj = new CmsSetDataSetValuesResponsePDU(null);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetDataSetValuesResponsePDU obj = new CmsSetDataSetValuesResponsePDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetDataSetValuesResponsePDU d = MAPPER.readValue(json, CmsSetDataSetValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
