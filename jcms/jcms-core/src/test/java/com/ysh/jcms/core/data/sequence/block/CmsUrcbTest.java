package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.sequence.block.CmsUrcb;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsUrcbTest {
    @Test
    public void roundup() {
        CmsUrcb a = new CmsUrcb().rptID("urpt01").rptEna(false).datSet("dataset1").confRev(5L)
                .optFlds(new CmsRcbOptFlds().sequence_number(true)).bufTm(3000L).sqNum(200)
                .trgOps(new CmsTriggerConditions().data_change(true)).intgPd(1000L).gi(false).resv(true);
        byte[] encoded = a.encode();
        CmsUrcb b = new CmsUrcb();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
