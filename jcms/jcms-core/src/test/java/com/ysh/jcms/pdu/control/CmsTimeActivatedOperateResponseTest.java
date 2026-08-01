package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTimeActivatedOperateResponseTest {
    @Test
    public void roundup() {
        CmsTimeActivatedOperateResponse a = new CmsTimeActivatedOperateResponse()
            .reference("ref".getBytes())
            .ctlVal(new CmsData().alt_boolean(true))
            .operTm(new CmsUtcTime().secondsSinceEpoch(1000L))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(4)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(456L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)))
            .test(true)
            .check(new CmsCheck().syncheck(true).interlock_check(false));
        byte[] encoded = a.encode();

        CmsTimeActivatedOperateResponse b = new CmsTimeActivatedOperateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
