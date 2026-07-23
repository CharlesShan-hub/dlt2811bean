// Auto-generated. Tests for CmsReleaseResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsReleaseResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsReleaseResponsePDU obj = new CmsReleaseResponsePDU();
        assertNull(obj.association_id);
        assertEquals(0, obj.service_error);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsReleaseResponsePDU obj = new CmsReleaseResponsePDU();
        obj.association_id = new byte[]{0x01, 0x02};
        obj.service_error = 1;
        String json = MAPPER.writeValueAsString(obj);
        CmsReleaseResponsePDU d = MAPPER.readValue(json, CmsReleaseResponsePDU.class);
        assertEquals(obj, d);
    }
}
