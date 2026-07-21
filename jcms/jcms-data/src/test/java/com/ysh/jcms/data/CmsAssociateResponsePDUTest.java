// Auto-generated. Tests for CmsAssociateResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateResponsePDU obj = new CmsAssociateResponsePDU();
        assertNull(obj.association_id);
        assertEquals(0, obj.service_error);
        assertNull(obj.authentication_parameter);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateResponsePDU obj = new CmsAssociateResponsePDU();
        obj.association_id = new byte[0];
        obj.service_error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateResponsePDU d = MAPPER.readValue(json, CmsAssociateResponsePDU.class);
        assertEquals(obj, d);
    }
}
