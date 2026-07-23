// Auto-generated. Tests for CmsGetGoReferenceResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGoReferenceResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGoReferenceResponsePDU obj = new CmsGetGoReferenceResponsePDU();
        assertNull(obj.gocb_reference);
        assertEquals(0, obj.conf_rev);
        assertNull(obj.dat_set);
        assertNull(obj.member_data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGoReferenceResponsePDU obj = new CmsGetGoReferenceResponsePDU();
        obj.gocb_reference = "test";
        obj.conf_rev = 1;
        obj.dat_set = "test";
        obj.member_data = java.util.Collections.singletonList(new CmsAnonymousGetGoReferenceResponsePDUMemberData());
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGoReferenceResponsePDU d = MAPPER.readValue(json, CmsGetGoReferenceResponsePDU.class);
        assertEquals(obj, d);
    }
}
