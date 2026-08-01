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

public class CmsOperateErrorTest {
    @Test
    public void roundup() {
        CmsOperateError a = new CmsOperateError()
            .reference("err3".getBytes())
            .ctlVal(new CmsData().alt_int32(42))
            .origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin".getBytes()))
            .ctlNum(1)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(2000000L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true)))
            .test(false)
            .check(new CmsCheck().syncheck(true).interlock_check(true))
            .addCause(CmsAddCause.COMMAND_ALREADY_IN_EXECUTION);
        byte[] encoded = a.encode();

        CmsOperateError b = new CmsOperateError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
