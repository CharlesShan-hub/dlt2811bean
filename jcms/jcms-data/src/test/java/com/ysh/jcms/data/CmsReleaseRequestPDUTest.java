// Auto-generated. Tests for CmsReleaseRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsReleaseRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsReleaseRequestPDU obj = new CmsReleaseRequestPDU();
        assertNull(obj.association_id);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsReleaseRequestPDU obj = new CmsReleaseRequestPDU();
        obj.association_id = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsReleaseRequestPDU d = MAPPER.readValue(json, CmsReleaseRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsReleaseRequestPDU obj = new CmsReleaseRequestPDU();
        obj.association_id = new byte[0];
        byte[] data = obj.encode("uper");
        CmsReleaseRequestPDU d = CmsReleaseRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
