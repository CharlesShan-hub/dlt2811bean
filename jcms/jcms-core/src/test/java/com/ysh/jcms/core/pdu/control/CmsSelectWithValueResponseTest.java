package com.ysh.jcms.core.pdu.control;

import com.ysh.jcms.core.data.bitarray.CmsCheck;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.enumerate.CmsOrCat;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectWithValueResponseTest {
    @Test
    public void roundup() {
        CmsSelectWithValueResponse a = new CmsSelectWithValueResponse()
            .reference("ref".getBytes())
            .ctlVal(new CmsData().alt_int32(88))
            .operTm(new CmsUtcTime().secondsSinceEpoch(123L))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(5)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(456L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)))
            .test(true)
            .check(new CmsCheck().syncheck(true).interlock_check(false));
        byte[] encoded = a.encode();

        CmsSelectWithValueResponse b = new CmsSelectWithValueResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
