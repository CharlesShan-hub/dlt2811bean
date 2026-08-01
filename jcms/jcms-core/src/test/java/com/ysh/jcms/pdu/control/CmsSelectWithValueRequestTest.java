package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectWithValueRequestTest {
    @Test
    public void roundup() {
        CmsSelectWithValueRequest a = new CmsSelectWithValueRequest()
            .reference("ref2".getBytes())
            .ctlVal(new CmsData().alt_boolean(true))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin1".getBytes()))
            .ctlNum(5)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(1000000L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)))
            .test(false)
            .check(new CmsCheck().syncheck(true).interlock_check(false));
        byte[] encoded = a.encode();

        CmsSelectWithValueRequest b = new CmsSelectWithValueRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
