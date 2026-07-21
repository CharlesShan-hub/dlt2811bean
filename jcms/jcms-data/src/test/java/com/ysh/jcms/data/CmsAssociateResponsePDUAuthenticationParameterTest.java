// Auto-generated. Tests for CmsAssociateResponsePDUAuthenticationParameter

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateResponsePDUAuthenticationParameterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateResponsePDUAuthenticationParameter obj = new CmsAssociateResponsePDUAuthenticationParameter();
        assertNull(obj.signature_certificate);
        assertNull(obj.signed_time);
        assertNull(obj.signed_value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateResponsePDUAuthenticationParameter obj = new CmsAssociateResponsePDUAuthenticationParameter();
        obj.signature_certificate = new byte[]{0x01, 0x02};
        obj.signed_time = new byte[]{0x01, 0x02};
        obj.signed_value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateResponsePDUAuthenticationParameter d = MAPPER.readValue(json, CmsAssociateResponsePDUAuthenticationParameter.class);
        assertEquals(obj, d);
    }
}
