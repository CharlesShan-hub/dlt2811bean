// Auto-generated. Tests for CmsReasonCode

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsReasonCodeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsReasonCode obj = new CmsReasonCode();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsReasonCode obj = new CmsReasonCode(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsReasonCode obj = new CmsReasonCode(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsReasonCode d = MAPPER.readValue(json, CmsReasonCode.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsReasonCode obj = new CmsReasonCode(42);
        byte[] data = obj.encode("uper");
        CmsReasonCode d = CmsReasonCode.decode("uper", data);
        assertEquals(obj, d);
    }
}
