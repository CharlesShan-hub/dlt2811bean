// Auto-generated. Tests for CmsQuality

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsQualityTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsQuality obj = new CmsQuality();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsQuality obj = new CmsQuality(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsQuality obj = new CmsQuality(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsQuality d = MAPPER.readValue(json, CmsQuality.class);
        assertEquals(obj, d);
    }
}
