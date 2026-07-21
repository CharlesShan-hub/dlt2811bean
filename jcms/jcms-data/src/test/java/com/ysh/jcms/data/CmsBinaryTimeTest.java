// Auto-generated. Tests for CmsBinaryTime

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsBinaryTimeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsBinaryTime obj = new CmsBinaryTime();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsBinaryTime obj = new CmsBinaryTime();
        obj.value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsBinaryTime d = MAPPER.readValue(json, CmsBinaryTime.class);
        assertEquals(obj, d);
    }
}
