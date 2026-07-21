// Auto-generated. Tests for CmsAssociateRequestPDUAuthenticationParameter

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateRequestPDUAuthenticationParameterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateRequestPDUAuthenticationParameter obj = new CmsAssociateRequestPDUAuthenticationParameter();
        assertNull(obj.signature_certificate);
        assertNull(obj.signed_time);
        assertNull(obj.signed_value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateRequestPDUAuthenticationParameter obj = new CmsAssociateRequestPDUAuthenticationParameter();
        obj.signature_certificate = new byte[0];
        obj.signed_time = new byte[0];
        obj.signed_value = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateRequestPDUAuthenticationParameter d = MAPPER.readValue(json, CmsAssociateRequestPDUAuthenticationParameter.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAssociateRequestPDUAuthenticationParameter obj = new CmsAssociateRequestPDUAuthenticationParameter();
        obj.signature_certificate = new byte[0];
        obj.signed_time = new byte[0];
        obj.signed_value = new byte[0];
        byte[] data = obj.encode("uper");
        CmsAssociateRequestPDUAuthenticationParameter d = CmsAssociateRequestPDUAuthenticationParameter.decode("uper", data);
        assertEquals(obj, d);
    }
}
