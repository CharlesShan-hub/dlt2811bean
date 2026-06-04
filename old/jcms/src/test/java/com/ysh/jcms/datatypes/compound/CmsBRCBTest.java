package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBRCB")
class CmsBRCBTest {

    @Test
    void roundtrip() {
        CmsBRCB original = new CmsBRCB();
        original.rptID = "BRCB_001";
        original.rptEna = true;
        original.datSet = "DataSet_01";
        original.confRev = 1;
        original.optFlds = new byte[]{0x01, 0x00};
        original.bufTm = 1000;
        original.sqNum = 5;
        original.trgOps = new byte[]{0x07};
        original.intgPd = 5000;
        original.gi = false;
        original.purgeBuf = true;
        original.entryID = new byte[]{0, 0, 0, 0, 1, 2, 3, 4};
        original.timeOfEntry = new CmsBinaryTime(12, 30, 0, 0, 0);

        byte[] data = original.encode();
        CmsBRCB decoded = CmsBRCB.from(data);

        assertEquals(original.rptID.trim(), decoded.rptID.trim());
        assertEquals(original.rptEna, decoded.rptEna);
        assertEquals(original.confRev, decoded.confRev);
        assertEquals(original.bufTm, decoded.bufTm);
        assertEquals(original.sqNum, decoded.sqNum);
        assertEquals(original.gi, decoded.gi);
        assertEquals(original.purgeBuf, decoded.purgeBuf);
    }

    @Test
    void copy() {
        CmsBRCB original = new CmsBRCB();
        original.rptID = "BRCB_COPY";
        original.rptEna = true;
        original.confRev = 2;
        original.bufTm = 2000;

        CmsBRCB cloned = original.copy();
        assertEquals(original.rptID, cloned.rptID);
        assertEquals(original.rptEna, cloned.rptEna);
        assertEquals(original.confRev, cloned.confRev);
        assertEquals(original.bufTm, cloned.bufTm);
    }
}
