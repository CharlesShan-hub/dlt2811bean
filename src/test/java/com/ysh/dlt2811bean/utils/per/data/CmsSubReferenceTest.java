package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsSubReferenceTest {

    @Test
    void encodeDecode_basic() throws Exception {
        String ref = "LLN0.MMXU.UmxInst.mag.f";
        PerOutputStream pos = new PerOutputStream();
        CmsSubReference.encode(pos, ref);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        assertEquals(ref, CmsSubReference.decode(pis));
    }

    @Test
    void encodeDecode_empty() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsSubReference.encode(pos, "");
        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("", CmsSubReference.decode(pis));
    }

    @Test
    void encode_nullBecomesEmpty() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsSubReference.encode(pos, null);
        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals("", CmsSubReference.decode(pis));
    }

    @Test
    void encode_rejectsTooLong() {
        String ref = "A".repeat(130);
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsSubReference.encode(pos, ref));
    }

    @Test
    void encodeDecode_129chars() throws Exception {
        String ref = "A".repeat(129);
        PerOutputStream pos = new PerOutputStream();
        CmsSubReference.encode(pos, ref);
        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(ref, CmsSubReference.decode(pis));
    }
}
