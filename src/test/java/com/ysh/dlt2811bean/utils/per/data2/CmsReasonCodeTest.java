package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsReasonCodeTest {

    @Test
    void default_isAllFalse() {
        CmsReasonCode rc = new CmsReasonCode();
        assertFalse(rc.is(CmsReasonCode.DATA_CHANGE));
        assertFalse(rc.is(CmsReasonCode.INTEGRITY));
        assertFalse(rc.is(CmsReasonCode.APPLICATION_TRIGGER));
        assertFalse(rc.hasAnyReason());
        assertEquals(0, rc.toRaw());
    }

    @Test
    void set_singleBit() {
        CmsReasonCode rc = new CmsReasonCode();
        rc.set(CmsReasonCode.DATA_CHANGE, true);

        assertTrue(rc.is(CmsReasonCode.DATA_CHANGE));
        assertFalse(rc.is(CmsReasonCode.QUALITY_CHANGE));
        assertEquals(0x02, rc.toRaw());
    }

    @Test
    void set_multipleBits() {
        CmsReasonCode rc = new CmsReasonCode();
        rc.set(CmsReasonCode.DATA_CHANGE, true);
        rc.set(CmsReasonCode.INTEGRITY, true);
        rc.set(CmsReasonCode.APPLICATION_TRIGGER, true);

        assertTrue(rc.is(CmsReasonCode.DATA_CHANGE));
        assertTrue(rc.is(CmsReasonCode.INTEGRITY));
        assertTrue(rc.is(CmsReasonCode.APPLICATION_TRIGGER));
        assertFalse(rc.is(CmsReasonCode.QUALITY_CHANGE));
        assertEquals(0x52, rc.toRaw()); // bits 1,4,6
        assertTrue(rc.hasAnyReason());
    }

    @Test
    void set_thenClear() {
        CmsReasonCode rc = new CmsReasonCode();
        rc.set(CmsReasonCode.DATA_CHANGE, true);
        assertTrue(rc.is(CmsReasonCode.DATA_CHANGE));

        rc.set(CmsReasonCode.DATA_CHANGE, false);
        assertFalse(rc.is(CmsReasonCode.DATA_CHANGE));
        assertEquals(0, rc.toRaw());
    }

    @Test
    void construct_fromRaw() {
        CmsReasonCode rc = new CmsReasonCode(0x12); // bits 1,4
        assertTrue(rc.is(CmsReasonCode.DATA_CHANGE));
        assertTrue(rc.is(CmsReasonCode.INTEGRITY));
        assertFalse(rc.is(CmsReasonCode.QUALITY_CHANGE));
    }

    @Test
    void encodeDecode() throws Exception {
        CmsReasonCode rc = new CmsReasonCode();
        rc.set(CmsReasonCode.DATA_CHANGE, true);
        rc.set(CmsReasonCode.GENERAL_INTERROGATION, true);

        PerOutputStream pos = new PerOutputStream();
        CmsReasonCode.encode(pos, rc);

        CmsReasonCode r = CmsReasonCode.decode(new PerInputStream(pos.toByteArray()));
        assertTrue(r.is(CmsReasonCode.DATA_CHANGE));
        assertTrue(r.is(CmsReasonCode.GENERAL_INTERROGATION));
        assertFalse(r.is(CmsReasonCode.INTEGRITY));
        assertEquals(rc.toRaw(), r.toRaw());
    }

    @Test
    void is_outOfRange_throws() {
        CmsReasonCode rc = new CmsReasonCode();
        assertThrows(IllegalArgumentException.class, () -> rc.is(99));
        assertThrows(IllegalArgumentException.class, () -> rc.is(-1));
    }
}
