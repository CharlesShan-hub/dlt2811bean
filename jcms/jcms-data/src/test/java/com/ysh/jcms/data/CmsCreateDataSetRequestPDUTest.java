// Auto-generated. Tests for CmsCreateDataSetRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsCreateDataSetRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsCreateDataSetRequestPDU obj = new CmsCreateDataSetRequestPDU();
        assertNull(obj.dataset_reference);
        assertNull(obj.reference_after);
        assertNull(obj.member_data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsCreateDataSetRequestPDU obj = new CmsCreateDataSetRequestPDU();
        obj.dataset_reference = "test";
        obj.reference_after = "test";
        obj.member_data = java.util.Collections.singletonList(new CmsAnonymousCreateDataSetRequestPDUMemberData());
        String json = MAPPER.writeValueAsString(obj);
        CmsCreateDataSetRequestPDU d = MAPPER.readValue(json, CmsCreateDataSetRequestPDU.class);
        assertEquals(obj, d);
    }
}
