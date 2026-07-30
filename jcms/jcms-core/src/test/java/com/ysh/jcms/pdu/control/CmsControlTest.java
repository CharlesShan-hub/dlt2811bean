package com.ysh.jcms.pdu.control;

import com.ysh.jcms.data.bitarray.CmsCheck;
import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsOrCat;
import com.ysh.jcms.data.sequence.common.CmsOriginator;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsControlTest {

    @Test
    public void select_roundup() {
        CmsSelectRequest a = new CmsSelectRequest().reference("ref1".getBytes());
        byte[] encoded = a.encode();

        CmsSelectRequest b = new CmsSelectRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void select_with_value_roundup() {
        CmsSelectWithValueRequest a = new CmsSelectWithValueRequest()
            .reference("ref2".getBytes());
        a.ctlVal.alt_boolean(true);
        a.origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin1".getBytes()))
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

    @Test
    public void operate_roundup() {
        CmsOperateRequest a = new CmsOperateRequest()
            .reference("ref3".getBytes());
        a.ctlVal.alt_int32(42);
        a.origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("op".getBytes()))
            .ctlNum(1)
            .t(new CmsUtcTime()
                .secondsSinceEpoch(2000000L)
                .fractionOfSecond(0)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true)))
            .test(false)
            .check(new CmsCheck().syncheck(true).interlock_check(true));
        byte[] encoded = a.encode();

        CmsOperateRequest b = new CmsOperateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
