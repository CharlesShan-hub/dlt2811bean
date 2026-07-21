// Auto-generated. Tests for CmsRcbOptFlds

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsRcbOptFldsTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsRcbOptFlds obj = new CmsRcbOptFlds();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsRcbOptFlds obj = new CmsRcbOptFlds(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsRcbOptFlds obj = new CmsRcbOptFlds(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsRcbOptFlds d = MAPPER.readValue(json, CmsRcbOptFlds.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsRcbOptFlds obj = new CmsRcbOptFlds(42);
        byte[] data = obj.encode("uper");
        CmsRcbOptFlds d = CmsRcbOptFlds.decode("uper", data);
        assertEquals(obj, d);
    }
}
