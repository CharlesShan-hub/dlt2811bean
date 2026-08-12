package com.ysh.jcms.core.pdu.control;

import com.ysh.jcms.core.data.bitarray.CmsCheck;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.enumerate.CmsOrCat;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTimeActivatedOperateRequestTest {
    @Test
    public void roundup() {
        CmsTimeActivatedOperateRequest a = new CmsTimeActivatedOperateRequest()
            .reference("ref".getBytes())
            .ctlVal(new CmsData().alt_int32(7))
            .operTm(new CmsUtcTime().secondsSinceEpoch(1000L))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(4)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(456L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true)))
            .test(false)
            .check(new CmsCheck().syncheck(true).interlock_check(true));
        byte[] encoded = a.encode();

        CmsTimeActivatedOperateRequest b = new CmsTimeActivatedOperateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
