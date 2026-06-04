package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsSGCB")
class CmsSGCBTest {

    @Test
    void roundtrip() {
        CmsSGCB original = new CmsSGCB();
        original.numOfSG = 3;
        original.actSG = 1;
        original.editSG = 2;
        original.tActEdt = new CmsUtcTime();
        original.tActEdt.seconds_since_epoch = 1700000000L;
        original.tActEdt.fraction_of_second = 500_000_000;
        original.tActEdt.time_quality = 0;

        byte[] data = original.encode();
        CmsSGCB decoded = CmsSGCB.from(data);

        assertEquals(original.numOfSG, decoded.numOfSG);
        assertEquals(original.actSG, decoded.actSG);
        assertEquals(original.editSG, decoded.editSG);
        assertEquals(original.tActEdt.seconds_since_epoch, decoded.tActEdt.seconds_since_epoch);
    }

    @Test
    void withResvTms() {
        CmsSGCB original = new CmsSGCB();
        original.numOfSG = 5;
        original.actSG = 0;
        original.editSG = 0;
        original.tActEdt = new CmsUtcTime();
        original.resvTms = 100;

        byte[] data = original.encode();
        CmsSGCB decoded = CmsSGCB.from(data);

        assertEquals(original.numOfSG, decoded.numOfSG);
        assertNotNull(decoded.resvTms);
        assertEquals(original.resvTms, decoded.resvTms);
    }

    @Test
    void copy() {
        CmsSGCB original = new CmsSGCB();
        original.numOfSG = 4;
        original.actSG = 2;
        original.editSG = 3;
        original.tActEdt = new CmsUtcTime();
        original.tActEdt.seconds_since_epoch = 1800000000L;

        CmsSGCB cloned = original.copy();
        assertEquals(original.numOfSG, cloned.numOfSG);
        assertEquals(original.actSG, cloned.actSG);
        assertEquals(original.editSG, cloned.editSG);
        assertEquals(original.tActEdt.seconds_since_epoch, cloned.tActEdt.seconds_since_epoch);
    }
}
