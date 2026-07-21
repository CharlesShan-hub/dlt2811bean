// Auto-generated. Tests for CmsEntryTime

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsEntryTimeTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsEntryTime obj = new CmsEntryTime();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsEntryTime obj = new CmsEntryTime();
        obj.value = new byte[]{0x01, 0x02};
        String json = MAPPER.writeValueAsString(obj);
        CmsEntryTime d = MAPPER.readValue(json, CmsEntryTime.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsEntryTime obj = new CmsEntryTime();
        obj.value = new byte[]{0x01, 0x02};
        byte[] data = obj.encode("uper");
        CmsEntryTime d = CmsEntryTime.decode("uper", data);
        assertEquals(obj, d);
    }
}
