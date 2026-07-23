// Auto-generated. Tests for CmsMsvcbOptFlds

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsMsvcbOptFldsTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsMsvcbOptFlds obj = new CmsMsvcbOptFlds();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsMsvcbOptFlds obj = new CmsMsvcbOptFlds(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsMsvcbOptFlds obj = new CmsMsvcbOptFlds(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsMsvcbOptFlds d = MAPPER.readValue(json, CmsMsvcbOptFlds.class);
        assertEquals(obj, d);
    }
}
