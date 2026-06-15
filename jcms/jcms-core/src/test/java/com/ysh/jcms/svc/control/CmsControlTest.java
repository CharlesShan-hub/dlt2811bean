package com.ysh.jcms.svc.control;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.control.CmsOrCat;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsControlTest {

    @Test
    public void select_roundtrip() {
        CmsSelectRequest a = new CmsSelectRequest()
            .reqId(10)
            .reference("ref1".getBytes());
        byte[] encoded = a.encode();

        CmsSelectRequest b = new CmsSelectRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void select_with_value_roundtrip() {
        CmsSelectWithValueRequest a = new CmsSelectWithValueRequest()
            .reqId(20)
            .reference("ref2".getBytes());
        a.ctlVal.choice.value(CmsData.CHOICE_BOOLEAN);
        a.ctlVal.alt_boolean.value(true);
        a.operTmPresent.value(false);
        a.origin.orCat.value(CmsOrCat.NOT_SUPPORTED);
        a.origin.orIdent.value("origin1".getBytes());
        a.ctlNum.value(5);
        a.t.seconds_since_epoch.value(1000000L);
        a.t.fraction_of_second.value(0);
        a.t.time_quality.leap_seconds_known.value(false);
        a.test.value(false);
        a.check.syncheck.value(true);
        a.check.interlock_check.value(false);
        byte[] encoded = a.encode();

        CmsSelectWithValueRequest b = new CmsSelectWithValueRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void operate_roundtrip() {
        CmsOperateRequest a = new CmsOperateRequest()
            .reqId(30)
            .reference("ref3".getBytes());
        a.ctlVal.choice.value(CmsData.CHOICE_INT32);
        a.ctlVal.alt_int32.value(42);
        a.origin.orCat.value(CmsOrCat.NOT_SUPPORTED);
        a.origin.orIdent.value("op".getBytes());
        a.ctlNum.value(1);
        a.t.seconds_since_epoch.value(2000000L);
        a.t.fraction_of_second.value(0);
        a.t.time_quality.leap_seconds_known.value(true);
        a.test.value(false);
        a.check.syncheck.value(true);
        a.check.interlock_check.value(true);
        byte[] encoded = a.encode();

        CmsOperateRequest b = new CmsOperateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
