// Auto-generated. Tests for CmsSetMSVCBValuesRequestPDUMsvcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetMSVCBValuesRequestPDUMsvcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetMSVCBValuesRequestPDUMsvcb obj = new CmsSetMSVCBValuesRequestPDUMsvcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetMSVCBValuesRequestPDUMsvcb obj = new CmsSetMSVCBValuesRequestPDUMsvcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsSetMSVCBValuesRequestPDUMsvcb d = MAPPER.readValue(json, CmsSetMSVCBValuesRequestPDUMsvcb.class);
        assertEquals(obj, d);
    }
}
