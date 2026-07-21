// Auto-generated. Tests for CmsAnonymousGetLogStatusValuesResponsePDULogValue

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetLogStatusValuesResponsePDULogValueTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetLogStatusValuesResponsePDULogValue obj = new CmsAnonymousGetLogStatusValuesResponsePDULogValue();
        assertNull(obj.old_entr_tm);
        assertNull(obj.new_entr_tm);
        assertNull(obj.old_entr);
        assertNull(obj.new_entr);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetLogStatusValuesResponsePDULogValue obj = new CmsAnonymousGetLogStatusValuesResponsePDULogValue();
        obj.old_entr_tm = new byte[0];
        obj.new_entr_tm = new byte[0];
        obj.old_entr = new byte[0];
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetLogStatusValuesResponsePDULogValue d = MAPPER.readValue(json, CmsAnonymousGetLogStatusValuesResponsePDULogValue.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetLogStatusValuesResponsePDULogValue obj = new CmsAnonymousGetLogStatusValuesResponsePDULogValue();
        obj.old_entr_tm = new byte[0];
        obj.new_entr_tm = new byte[0];
        obj.old_entr = new byte[0];
        byte[] data = obj.encode("uper");
        CmsAnonymousGetLogStatusValuesResponsePDULogValue d = CmsAnonymousGetLogStatusValuesResponsePDULogValue.decode("uper", data);
        assertEquals(obj, d);
    }
}
