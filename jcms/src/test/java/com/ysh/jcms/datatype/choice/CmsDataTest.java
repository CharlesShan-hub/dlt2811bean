package com.ysh.jcms.datatype.choice;

import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.datatype.common.CmsDbpos;
import com.ysh.jcms.datatype.common.CmsTcmd;
import com.ysh.jcms.datatype.common.CmsQuality;
import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.control.CmsCheck;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import static com.ysh.jcms.datatype.choice.CmsDataType.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsData")
class CmsDataTest {

    @Test void type0Error() {
        assertEquals(CmsData.of(new CmsServiceError().value(CmsServiceError.INSTANCE_NOT_AVAILABLE)),
                     new CmsData().decode(CmsData.of(new CmsServiceError().value(CmsServiceError.INSTANCE_NOT_AVAILABLE)).encode()));
    }

    @Test void type3Boolean() {
        assertEquals(CmsData.of(new CmsBoolean().value(true)),
                     new CmsData().decode(CmsData.of(BOOLEAN, true).encode()));
        assertEquals(CmsData.of(BOOLEAN, true),
                     new CmsData().decode(CmsData.of(new CmsBoolean().value(true)).encode()));
    }

    @Test void type6Int32() {
        assertEquals(CmsData.of(INT32, -12345),
                     new CmsData().decode(CmsData.of(INT32, -12345).encode()));
    }

    @Test void type10Int32U() {
        assertEquals(CmsData.of(INT32U, 999999),
                     new CmsData().decode(CmsData.of(INT32U, 999999).encode()));
    }

    @Test void type13Float64() {
        assertEquals(CmsData.of(FLOAT64, 2.71828),
                     new CmsData().decode(CmsData.of(FLOAT64, 2.71828).encode()));
    }

    @Test void type18UtcTime() {
        assertEquals(CmsData.of(new CmsUtcTime().set(1718015445500L)),
                     new CmsData().decode(CmsData.of(new CmsUtcTime().set(1718015445500L)).encode()));
    }

    @Test void type19BinaryTime() {
        assertEquals(CmsData.of(new CmsBinaryTime().set(1718015445500L)),
                     new CmsData().decode(CmsData.of(new CmsBinaryTime().set(1718015445500L)).encode()));
    }

    @Test void type20Quality() {
        CmsQuality q = new CmsQuality();
        q.overflow().value(true);
        q.failure().value(true);
        assertEquals(CmsData.of(q),
                     new CmsData().decode(CmsData.of(q).encode()));
    }

    @Test void type21Dbpos() {
        assertEquals(CmsData.of(DBPOS, CmsDbpos.ON),
                     new CmsData().decode(CmsData.of(DBPOS, CmsDbpos.ON).encode()));
    }

    @Test void type22Tcmd() {
        assertEquals(CmsData.of(TCMD, CmsTcmd.SELECT),
                     new CmsData().decode(CmsData.of(TCMD, CmsTcmd.SELECT).encode()));
    }

    @Test void type23Check() {
        CmsCheck chk = new CmsCheck();
        chk.syncheck().value(true);
        chk.interlock_check().value(false);
        assertEquals(CmsData.of(chk),
                     new CmsData().decode(CmsData.of(chk).encode()));
    }

    @Test void defaultChoiceIsZero() {
        assertEquals(0, new CmsData().choice().value());
    }

    @Test void arrayOfTwoInt32() {
        CmsData decoded = new CmsData().decode(CmsData.array(
            CmsData.of(INT32, 42), CmsData.of(INT32, 99)).encode());
        assertEquals(ARRAY, decoded.choice().value());
        assertEquals(2, decoded.value.array.count);
    }
}
