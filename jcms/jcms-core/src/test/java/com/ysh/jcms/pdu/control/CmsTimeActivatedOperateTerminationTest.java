package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsAddCause;
import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTimeActivatedOperateTerminationTest {
    @Test
    public void roundup() {
        CmsTimeActivatedOperateTermination a = new CmsTimeActivatedOperateTermination()
            .reference("ref".getBytes())
            .ctlVal(new CmsData().alt_int32(7))
            .operTm(new CmsUtcTime().secondsSinceEpoch(1000L))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(4)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(456L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)))
            .test(true)
            .check(new CmsCheck().syncheck(true).interlock_check(true))
            .addCause(CmsAddCause.ABORTION_BY_COMMUNICATION_LOSS);
        byte[] encoded = a.encode();

        CmsTimeActivatedOperateTermination b = new CmsTimeActivatedOperateTermination();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
