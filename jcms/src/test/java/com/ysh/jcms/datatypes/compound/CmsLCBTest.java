package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLCB")
class CmsLCBTest {

    @Test
    void roundtrip() {
        CmsLCB original = new CmsLCB();
        original.logEna = true;
        original.datSet = "LogDataSet";
        original.trgOps = new byte[]{(byte) 0x40}; /* DATA_CHANGE = bit 1 */
        original.intgPd = 10000;
        original.logRef = "LogRef_001";

        byte[] data = original.encode();
        CmsLCB decoded = CmsLCB.from(data);

        assertEquals(original.logEna, decoded.logEna);
        assertEquals(original.datSet.trim(), decoded.datSet.trim());
        assertArrayEquals(original.trgOps, decoded.trgOps);
        assertEquals(original.intgPd, decoded.intgPd);
        assertEquals(original.logRef.trim(), decoded.logRef.trim());
    }

    @Test
    void withOptionalFields() {
        CmsLCB original = new CmsLCB();
        original.logEna = true;
        original.datSet = "LogDataSet";
        original.trgOps = new byte[]{(byte) 0xE0}; /* RESERVED + DATA_CHANGE + QUALITY_CHANGE */
        original.intgPd = 5000;
        original.logRef = "LogRef_002";
        original.optFlds = new byte[]{0x01};
        original.bufTm = 3000L;

        byte[] data = original.encode();
        CmsLCB decoded = CmsLCB.from(data);

        assertEquals(original.logEna, decoded.logEna);
        assertEquals(original.intgPd, decoded.intgPd);
        assertArrayNotNull(decoded.optFlds);
        assertEquals(original.bufTm, decoded.bufTm);
    }

    private static void assertArrayNotNull(byte[] arr) {
        assertNotNull(arr);
    }
}
