package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsURCB")
class CmsURCBTest {

    @Test
    void roundtrip() {
        CmsURCB original = new CmsURCB();
        original.rptID = "URCB_001";
        original.rptEna = true;
        original.datSet = "DataSet_URCB";
        original.confRev = 1;
        original.optFlds = new byte[]{0x01, 0x00};
        original.bufTm = 500;
        original.sqNum = 10;
        original.trgOps = new byte[]{0x03};
        original.intgPd = 2000;
        original.gi = false;

        byte[] data = original.encode();
        CmsURCB decoded = CmsURCB.from(data);

        assertEquals(original.rptID.trim(), decoded.rptID.trim());
        assertEquals(original.rptEna, decoded.rptEna);
        assertEquals(original.datSet.trim(), decoded.datSet.trim());
        assertEquals(original.confRev, decoded.confRev);
        assertEquals(original.bufTm, decoded.bufTm);
        assertEquals(original.sqNum, decoded.sqNum);
        assertEquals(original.intgPd, decoded.intgPd);
        assertEquals(original.gi, decoded.gi);
    }

    @Test
    void withOwner() {
        CmsURCB original = new CmsURCB();
        original.rptID = "URCB_002";
        original.rptEna = true;
        original.datSet = "DataSet_URCB";
        original.confRev = 2;
        original.optFlds = new byte[]{0x03, 0x00};
        original.bufTm = 1000;
        original.sqNum = 0;
        original.trgOps = new byte[]{0x07};
        original.intgPd = 5000;
        original.gi = true;
        original.resv = false;
        original.owner = "Owner_001".getBytes();

        byte[] data = original.encode();
        CmsURCB decoded = CmsURCB.from(data);

        assertEquals(original.rptID.trim(), decoded.rptID.trim());
        assertEquals(original.rptEna, decoded.rptEna);
        assertNotNull(decoded.owner);
        assertArrayEquals(original.owner, decoded.owner);
    }
}
