package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsObjectReferenceTest {

    // ==================== validate ====================

    @Test
    void validate_normal() {
        CmsObjectReference.validate("S1LD1/LLN0.Health");
    }

    @Test
    void validate_empty() {
        CmsObjectReference.validate("");
    }

    @Test
    void validate_null() {
        CmsObjectReference.validate(null);
    }

    @Test
    void validate_rejectsDollar() {
        assertThrows(IllegalArgumentException.class,
                () -> CmsObjectReference.validate("S1LD1/LLN0$Health"));
    }

    @Test
    void validate_rejectsFc_st() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> CmsObjectReference.validate("S1LD1/LLN0.Health.st"));
        assertTrue(ex.getMessage().contains("FC"));
    }

    @Test
    void validate_rejectsFc_cf() {
        assertThrows(IllegalArgumentException.class,
                () -> CmsObjectReference.validate("S1LD1/LLN0$CF.cf"));
    }

    @Test
    void validate_rejectsFc_caseInsensitive() {
        assertThrows(IllegalArgumentException.class,
                () -> CmsObjectReference.validate("S1LD1/LLN0.Health.mx"));
    }

    @Test
    void validate_allowsDotNotFc() {
        // ".Health" is not an FC suffix, should pass
        CmsObjectReference.validate("S1LD1/LLN0.Health.Health");
    }

    // ==================== encode/decode ====================

    @Test
    void encodeDecode_basic() throws Exception {
        String ref = "S1LD1/LLN0.Health";

        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, ref);
        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(ref, CmsObjectReference.decode(pis));
    }

    @Test
    void encodeDecode_empty() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, "");
        assertEquals("", CmsObjectReference.decode(new PerInputStream(pos.toByteArray())));
    }

    @Test
    void encodeDecode_nullBecomesEmpty() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, null);
        assertEquals("", CmsObjectReference.decode(new PerInputStream(pos.toByteArray())));
    }

    @Test
    void encodeDecode_maxLength() throws Exception {
        String ref = "LD" + "/LN" + "." + "A".repeat(121);
        PerOutputStream pos = new PerOutputStream();
        CmsObjectReference.encode(pos, ref);
        assertEquals(ref, CmsObjectReference.decode(new PerInputStream(pos.toByteArray())));
    }

    @Test
    void encode_rejectsTooLong() {
        String ref = "LD" + "/LN" + "." + "A".repeat(124);
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsObjectReference.encode(pos, ref));
    }

    @Test
    void encode_rejectsDollar() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
                () -> CmsObjectReference.encode(pos, "LD1/LLN0$Inst.ctlVal"));
    }

    @Test
    void encode_rejectsFcSuffix() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
                () -> CmsObjectReference.encode(pos, "LD1/LLN0.Health.st"));
    }

    @Test
    void maxLength_is129() {
        assertEquals(129, CmsObjectReference.MAX_LENGTH);
    }
}
