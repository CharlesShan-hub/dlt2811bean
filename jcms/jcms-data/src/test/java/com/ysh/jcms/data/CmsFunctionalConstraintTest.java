// Auto-generated. Tests for CmsFunctionalConstraint

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsFunctionalConstraintTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsFunctionalConstraint obj = new CmsFunctionalConstraint();
        assertNull(obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsFunctionalConstraint obj = new CmsFunctionalConstraint("hello");
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsFunctionalConstraint obj = new CmsFunctionalConstraint("test");
        obj.value = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsFunctionalConstraint d = MAPPER.readValue(json, CmsFunctionalConstraint.class);
        assertEquals(obj, d);
    }
}
