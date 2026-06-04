package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsGoCB")
class CmsGoCBTest {

    @Test
    void roundtrip() {
        CmsGoCB original = new CmsGoCB();
        original.goEna = true;
        original.goID = "GoCB_001";
        original.datSet = "DataSet_Go";
        original.confRev = 3;
        original.ndsCom = false;

        byte[] data = original.encode();
        CmsGoCB decoded = CmsGoCB.from(data);

        assertEquals(original.goEna, decoded.goEna);
        assertEquals(original.goID.trim(), decoded.goID.trim());
        assertEquals(original.datSet.trim(), decoded.datSet.trim());
        assertEquals(original.confRev, decoded.confRev);
        assertEquals(original.ndsCom, decoded.ndsCom);
    }

    @Test
    void withDstAddress() {
        CmsGoCB original = new CmsGoCB();
        original.goEna = true;
        original.goID = "GoCB_002";
        original.datSet = "DataSet_Go";
        original.confRev = 1;
        original.ndsCom = true;
        original.dstAddr = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        original.dstPriority = 4;
        original.dstVid = 100;
        original.dstAppId = 200;

        byte[] data = original.encode();
        CmsGoCB decoded = CmsGoCB.from(data);

        assertEquals(original.goEna, decoded.goEna);
        assertEquals(original.ndsCom, decoded.ndsCom);
        assertArrayEquals(original.dstAddr, decoded.dstAddr);
        assertEquals(original.dstPriority, decoded.dstPriority);
        assertEquals(original.dstVid, decoded.dstVid);
        assertEquals(original.dstAppId, decoded.dstAppId);
    }
}
