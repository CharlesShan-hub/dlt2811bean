// Auto-generated. Tests for CmsAssociateNegotiateErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAssociateNegotiateErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAssociateNegotiateErrorPDU obj = new CmsAssociateNegotiateErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsAssociateNegotiateErrorPDU obj = new CmsAssociateNegotiateErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAssociateNegotiateErrorPDU obj = new CmsAssociateNegotiateErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsAssociateNegotiateErrorPDU d = MAPPER.readValue(json, CmsAssociateNegotiateErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAssociateNegotiateErrorPDU obj = new CmsAssociateNegotiateErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsAssociateNegotiateErrorPDU d = CmsAssociateNegotiateErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
