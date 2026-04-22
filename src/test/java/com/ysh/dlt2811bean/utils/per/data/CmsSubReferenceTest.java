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
    void validate_allowsDot() {
        CmsSubReference.validate("LLN0.MMXU.UmxInst.mag.f");
    }

    @Test
    void validate_allowsEmpty() {
        CmsSubReference.validate("");
    }

    @Test
    void validate_allowsNull() {
        CmsSubReference.validate(null);
    }

    @Test
    void validate_rejectsSlash() {
        assertThrows(IllegalArgumentException.class,
                () -> CmsSubReference.validate("LD1/LLN0.MMXU.UmxInst.mag.f"));
    }

    @Test
    void encode_rejectsSlash() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
                () -> CmsSubReference.encode(pos, "LD1/LLN0.Health"));
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
