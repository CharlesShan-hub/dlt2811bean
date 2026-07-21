// Auto-generated. Tests for CmsTimeQuality

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsTimeQualityTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsTimeQuality obj = new CmsTimeQuality();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsTimeQuality obj = new CmsTimeQuality(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsTimeQuality obj = new CmsTimeQuality(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsTimeQuality d = MAPPER.readValue(json, CmsTimeQuality.class);
        assertEquals(obj, d);
    }
}
