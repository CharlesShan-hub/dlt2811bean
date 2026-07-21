// Auto-generated. Tests for CmsGetSGCBValuesResponsePDUSgscb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetSGCBValuesResponsePDUSgscbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetSGCBValuesResponsePDUSgscb obj = new CmsGetSGCBValuesResponsePDUSgscb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetSGCBValuesResponsePDUSgscb obj = new CmsGetSGCBValuesResponsePDUSgscb();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetSGCBValuesResponsePDUSgscb d = MAPPER.readValue(json, CmsGetSGCBValuesResponsePDUSgscb.class);
        assertEquals(obj, d);
    }
}
