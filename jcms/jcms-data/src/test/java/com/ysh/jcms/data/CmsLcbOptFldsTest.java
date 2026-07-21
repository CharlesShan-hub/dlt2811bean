// Auto-generated. Tests for CmsLcbOptFlds

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsLcbOptFldsTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsLcbOptFlds obj = new CmsLcbOptFlds();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsLcbOptFlds obj = new CmsLcbOptFlds(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsLcbOptFlds obj = new CmsLcbOptFlds(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsLcbOptFlds d = MAPPER.readValue(json, CmsLcbOptFlds.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsLcbOptFlds obj = new CmsLcbOptFlds(42);
        byte[] data = obj.encode("uper");
        CmsLcbOptFlds d = CmsLcbOptFlds.decode("uper", data);
        assertEquals(obj, d);
    }
}
