// Auto-generated. Tests for CmsSetDataSetValuesRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSetDataSetValuesRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSetDataSetValuesRequestPDU obj = new CmsSetDataSetValuesRequestPDU();
        assertNull(obj.dataset_reference);
        assertNull(obj.reference_after);
        assertNotNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSetDataSetValuesRequestPDU obj = new CmsSetDataSetValuesRequestPDU();
        obj.dataset_reference = "test";
        obj.reference_after = "test";
        obj.value = java.util.Collections.singletonList(new CmsData());
        String json = MAPPER.writeValueAsString(obj);
        CmsSetDataSetValuesRequestPDU d = MAPPER.readValue(json, CmsSetDataSetValuesRequestPDU.class);
        assertEquals(obj, d);
    }
}
