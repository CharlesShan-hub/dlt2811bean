package com.ysh.jcms.core.pdu.control;

import com.ysh.jcms.core.data.bitarray.CmsCheck;
import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.enumerate.CmsAddCause;
import com.ysh.jcms.core.data.enumerate.CmsOrCat;
import com.ysh.jcms.core.data.sequence.common.CmsOriginator;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectWithValueErrorTest {
    @Test
    public void roundup() {
        CmsSelectWithValueError a = new CmsSelectWithValueError()
            .reference("ref".getBytes())
            .ctlVal(new CmsData().alt_boolean(true))
            .operTm(new CmsUtcTime().secondsSinceEpoch(123L))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(5)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(456L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(false)))
            .test(true)
            .check(new CmsCheck().syncheck(true).interlock_check(true))
            .addCause(CmsAddCause.SELECT_FAILED);
        byte[] encoded = a.encode();

        CmsSelectWithValueError b = new CmsSelectWithValueError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
