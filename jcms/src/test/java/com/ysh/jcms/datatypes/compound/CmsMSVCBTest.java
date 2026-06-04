package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsMSVCB")
class CmsMSVCBTest {

    @Test
    void roundtrip() {
        CmsMSVCB original = new CmsMSVCB();
        original.svEna = true;
        original.msvID = "MSVCB_001";
        original.datSet = "MSDataSet";
        original.confRev = 1;
        original.smpRate = 80;
        original.optFlds = new byte[]{0x01};

        byte[] data = original.encode();
        CmsMSVCB decoded = CmsMSVCB.from(data);

        assertEquals(original.svEna, decoded.svEna);
        assertEquals(original.msvID.trim(), decoded.msvID.trim());
        assertEquals(original.datSet.trim(), decoded.datSet.trim());
        assertEquals(original.confRev, decoded.confRev);
        assertEquals(original.smpRate, decoded.smpRate);
    }

    @Test
    void withSmpMod() {
        CmsMSVCB original = new CmsMSVCB();
        original.svEna = true;
        original.msvID = "MSVCB_002";
        original.datSet = "MSDataSet";
        original.confRev = 2;
        original.smpMod = 1;
        original.smpRate = 4800;
        original.optFlds = new byte[]{0x03};

        byte[] data = original.encode();
        CmsMSVCB decoded = CmsMSVCB.from(data);

        assertEquals(original.svEna, decoded.svEna);
        assertNotNull(decoded.smpMod);
        assertEquals(original.smpMod, decoded.smpMod);
        assertEquals(original.smpRate, decoded.smpRate);
    }

    @Test
    void withDstAddress() {
        CmsMSVCB original = new CmsMSVCB();
        original.svEna = true;
        original.msvID = "MSVCB_003";
        original.datSet = "MSDataSet";
        original.confRev = 3;
        original.smpRate = 80;
        original.optFlds = new byte[]{0x01};
        original.dstAddr = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        original.dstPriority = 5;
        original.dstVid = 200;
        original.dstAppId = 300;

        byte[] data = original.encode();
        CmsMSVCB decoded = CmsMSVCB.from(data);

        assertEquals(original.svEna, decoded.svEna);
        assertArrayEquals(original.dstAddr, decoded.dstAddr);
        assertEquals(original.dstPriority, decoded.dstPriority);
        assertEquals(original.dstVid, decoded.dstVid);
        assertEquals(original.dstAppId, decoded.dstAppId);
    }
}
