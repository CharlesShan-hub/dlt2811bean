// Auto-generated. Tests for CmsAssociateRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateRequestPDU obj = new CmsAssociateRequestPDU();
        assertNull(obj.server_access_point_reference);
        assertNull(obj.authentication_parameter);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateRequestPDU obj = new CmsAssociateRequestPDU();
        obj.server_access_point_reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateRequestPDU d = MAPPER.readValue(json, CmsAssociateRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAssociateRequestPDU obj = new CmsAssociateRequestPDU();
        obj.server_access_point_reference = "test";
        byte[] data = obj.encode("uper");
        CmsAssociateRequestPDU d = CmsAssociateRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
