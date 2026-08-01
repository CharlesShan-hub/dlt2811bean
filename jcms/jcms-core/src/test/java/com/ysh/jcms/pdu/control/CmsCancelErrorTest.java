package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsAddCause;
import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsCancelErrorTest {
    @Test
    public void roundup() {
        CmsCancelError a = new CmsCancelError()
            .reqId(7)
            .reference("ref".getBytes())
            .ctlVal(new CmsData().alt_boolean(true))
            .operTm(new CmsUtcTime().secondsSinceEpoch(123L))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(3)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(456L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)))
            .test(true)
            .addCause(CmsAddCause.SELECT_FAILED);
        byte[] encoded = a.encode();

        CmsCancelError b = new CmsCancelError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
