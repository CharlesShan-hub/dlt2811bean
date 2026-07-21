// Auto-generated. Tests for CmsServiceError

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsServiceErrorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsServiceError obj = new CmsServiceError();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsServiceError obj = new CmsServiceError(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsServiceError obj = new CmsServiceError(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsServiceError d = MAPPER.readValue(json, CmsServiceError.class);
        assertEquals(obj, d);
    }
}
