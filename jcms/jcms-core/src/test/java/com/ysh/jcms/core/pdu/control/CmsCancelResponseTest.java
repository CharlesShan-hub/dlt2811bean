package com.ysh.jcms.core.pdu.control;

import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.enumerate.CmsOrCat;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsCancelResponseTest {
    @Test
    public void roundup() {
        CmsCancelResponse a = new CmsCancelResponse()
            .reference("ref".getBytes())
            .ctlVal(new CmsData().alt_boolean(false))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(3)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(456L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)))
            .test(true);
        byte[] encoded = a.encode();

        CmsCancelResponse b = new CmsCancelResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
