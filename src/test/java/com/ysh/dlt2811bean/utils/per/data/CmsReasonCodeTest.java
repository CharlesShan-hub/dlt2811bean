package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsReasonCode")
class CmsReasonCodeTest {

    @Test
    @DisplayName("default is all false")
    void default_isAllFalse() {
        CmsReasonCode rc = new CmsReasonCode();
        assertFalse(rc.testBit(CmsReasonCode.DATA_CHANGE));
        assertFalse(rc.testBit(CmsReasonCode.INTEGRITY));
        assertFalse(rc.testBit(CmsReasonCode.APPLICATION_TRIGGER));
        assertEquals(0L, rc.get());
    }

    @Test
    @DisplayName("set single bit")
    void set_singleBit() {
        CmsReasonCode rc = new CmsReasonCode();
        rc.setBit(CmsReasonCode.DATA_CHANGE, true);

        assertTrue(rc.testBit(CmsReasonCode.DATA_CHANGE));
        assertFalse(rc.testBit(CmsReasonCode.QUALITY_CHANGE));
        assertEquals(0x02L, rc.get());
    }

    @Test
    @DisplayName("set multiple bits")
    void set_multipleBits() {
        CmsReasonCode rc = new CmsReasonCode();
        rc.setBit(CmsReasonCode.DATA_CHANGE, true);
        rc.setBit(CmsReasonCode.INTEGRITY, true);
        rc.setBit(CmsReasonCode.APPLICATION_TRIGGER, true);

        assertTrue(rc.testBit(CmsReasonCode.DATA_CHANGE));
        assertTrue(rc.testBit(CmsReasonCode.INTEGRITY));
        assertTrue(rc.testBit(CmsReasonCode.APPLICATION_TRIGGER));
        assertFalse(rc.testBit(CmsReasonCode.QUALITY_CHANGE));
        assertEquals(0x52L, rc.get()); // bits 1,4,6
    }

    @Test
    @DisplayName("set then clear")
    void set_thenClear() {
        CmsReasonCode rc = new CmsReasonCode();
        rc.setBit(CmsReasonCode.DATA_CHANGE, true);
        assertTrue(rc.testBit(CmsReasonCode.DATA_CHANGE));

        rc.setBit(CmsReasonCode.DATA_CHANGE, false);
        assertFalse(rc.testBit(CmsReasonCode.DATA_CHANGE));
        assertEquals(0L, rc.get());
    }

    @Test
    @DisplayName("construct from raw value")
    void construct_fromRaw() {
        CmsReasonCode rc = new CmsReasonCode(0x12L); // bits 1,4
        assertTrue(rc.testBit(CmsReasonCode.DATA_CHANGE));
        assertTrue(rc.testBit(CmsReasonCode.INTEGRITY));
        assertFalse(rc.testBit(CmsReasonCode.QUALITY_CHANGE));
    }

    @Test
    @DisplayName("encode and decode")
    void encodeDecode() throws Exception {
        CmsReasonCode rc = new CmsReasonCode();
        rc.setBit(CmsReasonCode.DATA_CHANGE, true);
        rc.setBit(CmsReasonCode.GENERAL_INTERROGATION, true);

        PerOutputStream pos = new PerOutputStream();
        rc.encode(pos);

        CmsReasonCode r = new CmsReasonCode().decode(new PerInputStream(pos.toByteArray()));
        assertTrue(r.testBit(CmsReasonCode.DATA_CHANGE));
        assertTrue(r.testBit(CmsReasonCode.GENERAL_INTERROGATION));
        assertFalse(r.testBit(CmsReasonCode.INTEGRITY));
        assertEquals(rc.get(), r.get());
    }
}