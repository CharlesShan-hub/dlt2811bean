// Auto-generated. Tests for CmsGetMSVCBValuesResponsePDUMsvcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetMSVCBValuesResponsePDUMsvcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetMSVCBValuesResponsePDUMsvcb obj = new CmsGetMSVCBValuesResponsePDUMsvcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetMSVCBValuesResponsePDUMsvcb obj = new CmsGetMSVCBValuesResponsePDUMsvcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetMSVCBValuesResponsePDUMsvcb d = MAPPER.readValue(json, CmsGetMSVCBValuesResponsePDUMsvcb.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetMSVCBValuesResponsePDUMsvcb obj = new CmsGetMSVCBValuesResponsePDUMsvcb();
        byte[] data = obj.encode("uper");
        CmsGetMSVCBValuesResponsePDUMsvcb d = CmsGetMSVCBValuesResponsePDUMsvcb.decode("uper", data);
        assertEquals(obj, d);
    }
}
