// Auto-generated. Tests for CmsGetGoReferenceRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoReferenceRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoReferenceRequestPDU obj = new CmsGetGoReferenceRequestPDU();
        assertNull(obj.gocb_reference);
        assertNotNull(obj.member_ofs);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoReferenceRequestPDU obj = new CmsGetGoReferenceRequestPDU();
        obj.gocb_reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoReferenceRequestPDU d = MAPPER.readValue(json, CmsGetGoReferenceRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetGoReferenceRequestPDU obj = new CmsGetGoReferenceRequestPDU();
        obj.gocb_reference = "test";
        byte[] data = obj.encode("uper");
        CmsGetGoReferenceRequestPDU d = CmsGetGoReferenceRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
