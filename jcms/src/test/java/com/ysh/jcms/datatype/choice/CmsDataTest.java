package com.ysh.jcms.datatype.choice;

import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.datatype.common.CmsDbpos;
import com.ysh.jcms.datatype.common.CmsTcmd;
import com.ysh.jcms.datatype.common.CmsQuality;
import com.ysh.jcms.datatype.control.CmsCheck;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsData")
class CmsDataTest {

    private CmsData get() { return (CmsData)(new CmsData().test()); }

    @Test void type0Error() {
        CmsData original = (CmsData) CmsData.of(ERROR, CmsServiceError.INSTANCE_NOT_AVAILABLE).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type3Boolean() {
        CmsData original = (CmsData) CmsData.of(BOOLEAN, true).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type6Int32() {
        CmsData original = (CmsData) CmsData.of(INT32, -12345).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type10Int32U() {
        CmsData original = (CmsData) CmsData.of(INT32U, 999999).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type13Float64() {
        CmsData original = (CmsData) CmsData.of(FLOAT64, 2.71828).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type18UtcTime() {
        CmsUtcTime.ByValue v = (CmsUtcTime.ByValue) new CmsUtcTime.ByValue().set(1718015445500L);
        CmsData original = (CmsData) CmsData.of(v).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type19BinaryTime() {
        CmsBinaryTime.ByValue v = (CmsBinaryTime.ByValue) new CmsBinaryTime.ByValue().set(1718015445500L);
        CmsData original = (CmsData) CmsData.of(v).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type20Quality() {
        CmsQuality.ByValue q = new CmsQuality.ByValue();
        q.overflow.value = true;
        q.failure.value = true;
        CmsData original = (CmsData) CmsData.of(q).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type21Dbpos() {
        CmsData original = (CmsData) CmsData.of(DBPOS, CmsDbpos.ON).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type22Tcmd() {
        CmsData original = (CmsData) CmsData.of(TCMD, CmsTcmd.SELECT).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void type23Check() {
        CmsCheck.ByValue chk = new CmsCheck.ByValue();
        chk.syncheck.value = true;
        chk.interlock_check.value = false;
        CmsData original = (CmsData) CmsData.of(chk).test();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test void defaultChoiceIsZero() {
        assertEquals(0, get().choice().value());
    }

    @Test void arrayOfTwoInt32() {
        CmsData original = (CmsData) CmsData.array(
            CmsData.of(INT32, 42), CmsData.of(INT32, 99)).test();
        CmsData decoded = get().decode(original.encode());
        assertEquals(ARRAY, decoded.choice().value());
        assertEquals(2, decoded.value.array.count);
    }

    @Test void nestedArray() {
        CmsData inner1 = (CmsData) CmsData.array(CmsData.of(INT32, 1), CmsData.of(INT32, 2)).test();
        CmsData inner2 = (CmsData) CmsData.array(CmsData.of(INT32, 3), CmsData.of(INT32, 4)).test();
        CmsData outer = (CmsData) CmsData.array(inner1, inner2).test();

        CmsData decoded = get().decode(outer.encode());
        assertEquals(ARRAY, decoded.choice().value());
        assertEquals(2, decoded.value.array.count);
    }
}
