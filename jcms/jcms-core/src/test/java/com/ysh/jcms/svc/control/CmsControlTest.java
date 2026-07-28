package com.ysh.jcms.svc.control;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.control.CmsCheck;
import com.ysh.jcms.data.control.CmsOriginator;
import com.ysh.jcms.data.control.CmsOrCat;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsControlTest {

    @Test
    public void select_roundup() {
        CmsSelectRequest a = new CmsSelectRequest().reqId(10).reference("ref1".getBytes());
        byte[] encoded = a.encode();

        CmsSelectRequest b = new CmsSelectRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void select_with_value_roundup() {
        CmsSelectWithValueRequest a = new CmsSelectWithValueRequest().reqId(20).reference("ref2".getBytes());
        a.ctlVal.choice.value(CmsData.CHOICE_BOOLEAN);
        a.ctlVal.alt_boolean.value(true);
        a.operTmPresent.value(false);
        a.origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("origin1".getBytes()));
        a.ctlNum.value(5);
        a.t.secondsSinceEpoch.value(1000000L);
        a.t.fractionOfSecond.value(0);
        a.t.timeQuality.leap_seconds_known.value(false);
        a.test.value(false);
        a.check(new CmsCheck().syncheck(true).interlock_check(false));
        byte[] encoded = a.encode();

        CmsSelectWithValueRequest b = new CmsSelectWithValueRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void operate_roundup() {
        CmsOperateRequest a = new CmsOperateRequest().reqId(30).reference("ref3".getBytes());
        a.ctlVal.choice.value(CmsData.CHOICE_INT32);
        a.ctlVal.alt_int32.value(42);
        a.origin(new CmsOriginator().orCat(CmsOrCat.NOT_SUPPORTED).orIdent("op".getBytes()));
        a.ctlNum.value(1);
        a.t.secondsSinceEpoch.value(2000000L);
        a.t.fractionOfSecond.value(0);
        a.t.timeQuality.leap_seconds_known.value(true);
        a.test.value(false);
        a.check(new CmsCheck().syncheck(true).interlock_check(true));
        byte[] encoded = a.encode();

        CmsOperateRequest b = new CmsOperateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
