// Auto-generated. Tests for CmsAssociateNegotiateRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateNegotiateRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateNegotiateRequestPDU obj = new CmsAssociateNegotiateRequestPDU();
        assertEquals(0, obj.apdu_size);
        assertEquals(0, obj.asdu_size);
        assertEquals(0, obj.protocol_version);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateNegotiateRequestPDU obj = new CmsAssociateNegotiateRequestPDU();
        obj.apdu_size = 1;
        obj.asdu_size = 1;
        obj.protocol_version = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateNegotiateRequestPDU d = MAPPER.readValue(json, CmsAssociateNegotiateRequestPDU.class);
        assertEquals(obj, d);
    }
}
