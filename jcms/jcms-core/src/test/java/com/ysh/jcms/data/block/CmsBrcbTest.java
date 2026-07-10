package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsBrcbTest {
    @Test
    public void roundup() {
        CmsBrcb a = new CmsBrcb().rptID("rpt01".getBytes()).rptEna(true).datSet("dataset1".getBytes()).confRev(3L)
                .optFlds(new CmsRcbOptFlds().sequence_number(true)).bufTm(5000L).sqNum(100)
                .trgOps(new CmsTriggerConditions().data_change(true)).intgPd(3000L).gi(false).purgeBuf(true)
                .entryID(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        byte[] encoded = a.encode();

        CmsBrcb b = new CmsBrcb();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
