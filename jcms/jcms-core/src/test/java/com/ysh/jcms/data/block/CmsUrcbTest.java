package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsUrcbTest {
    @Test
    public void roundup() {
        CmsUrcb a = new CmsUrcb()
            .rptID("urpt01".getBytes())
            .rptEna(false)
            .datSet("dataset1".getBytes())
            .confRev(5L)
            .optFlds(new CmsRcbOptFlds().sequence_number(true))
            .bufTm(3000L)
            .sqNum(200)
            .trgOps(new CmsTriggerConditions().data_change(true))
            .intgPd(1000L)
            .gi(false)
            .resv(true);
        byte[] encoded = a.encode();
        CmsUrcb b = new CmsUrcb();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
