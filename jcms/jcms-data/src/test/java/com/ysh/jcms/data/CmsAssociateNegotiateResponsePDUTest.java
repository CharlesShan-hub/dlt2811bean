// Auto-generated. Tests for CmsAssociateNegotiateResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateNegotiateResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateNegotiateResponsePDU obj = new CmsAssociateNegotiateResponsePDU();
        assertEquals(0, obj.apdu_size);
        assertEquals(0, obj.asdu_size);
        assertEquals(0, obj.protocol_version);
        assertNull(obj.model_version);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateNegotiateResponsePDU obj = new CmsAssociateNegotiateResponsePDU();
        obj.apdu_size = 1;
        obj.asdu_size = 1;
        obj.protocol_version = 1;
        obj.model_version = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateNegotiateResponsePDU d = MAPPER.readValue(json, CmsAssociateNegotiateResponsePDU.class);
        assertEquals(obj, d);
    }
}
