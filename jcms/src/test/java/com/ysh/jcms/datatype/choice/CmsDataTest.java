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

    @Test void type0Error() {
        assertEquals(CmsData.of(ERROR, CmsServiceError.INSTANCE_NOT_AVAILABLE),
                     new CmsData().decode(CmsData.of(ERROR, CmsServiceError.INSTANCE_NOT_AVAILABLE).encode()));
    }

    @Test void type3Boolean() {
        assertEquals(CmsData.of(BOOLEAN, true),
                     new CmsData().decode(CmsData.of(BOOLEAN, true).encode()));
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
        CmsUtcTime.ByValue v = (CmsUtcTime.ByValue) new CmsUtcTime.ByValue().set(1718015445500L);
        System.out.println(CmsData.of(v));
        System.out.println(new CmsData().decode(CmsData.of(v).encode()));
        assertEquals(CmsData.of(v),
                     new CmsData().decode(CmsData.of(v).encode()));
    }

    @Test void type19BinaryTime() {
        CmsBinaryTime.ByValue v = (CmsBinaryTime.ByValue) new CmsBinaryTime.ByValue().set(1718015445500L);
        assertEquals(CmsData.of(v),
                     new CmsData().decode(CmsData.of(v).encode()));
    }

    @Test void type20Quality() {
        CmsQuality.ByValue q = new CmsQuality.ByValue();
        q.overflow.value = true;
        q.failure.value = true;
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
        CmsCheck.ByValue chk = new CmsCheck.ByValue();
        chk.syncheck.value = true;
        chk.interlock_check.value = false;
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

    @Test void nestedArray() {
        // 外层 ARRAY，内层每个元素也是一个 ARRAY（各有 2 个标量）
        CmsData inner1 = CmsData.array(CmsData.of(INT32, 1), CmsData.of(INT32, 2));
        CmsData inner2 = CmsData.array(CmsData.of(INT32, 3), CmsData.of(INT32, 4));
        CmsData outer = CmsData.array(inner1, inner2);

        CmsData decoded = new CmsData().decode(outer.encode());
        assertEquals(ARRAY, decoded.choice().value());
        CmsData[] outerElems = decoded.elements();
        assertEquals(2, outerElems.length);

        // 检查内层
        for (int i = 0; i < 2; i++) {
            assertEquals(ARRAY, outerElems[i].choice().value());
            CmsData[] innerElems = outerElems[i].elements();
            assertEquals(2, innerElems.length);
        }
    }

    @Test void nestedStructure() {
        // 外层 ARRAY，内层每个元素是一个 STRUCTURE（各有 2 个标量）
        CmsData inner1 = CmsData.structure(CmsData.of(INT32, 10), CmsData.of(BOOLEAN, true));
        CmsData inner2 = CmsData.structure(CmsData.of(INT32, 20), CmsData.of(BOOLEAN, false));
        CmsData outer = CmsData.array(inner1, inner2);

        CmsData decoded = new CmsData().decode(outer.encode());
        assertEquals(ARRAY, decoded.choice().value());
        CmsData[] outerElems = decoded.elements();
        assertEquals(2, outerElems.length);

        // 检查内层
        assertEquals(STRUCTURE, outerElems[0].choice().value());
        assertEquals(STRUCTURE, outerElems[1].choice().value());
        CmsData[] innerElems0 = outerElems[0].elements();
        assertEquals(2, innerElems0.length);
        CmsData[] innerElems1 = outerElems[1].elements();
        assertEquals(2, innerElems1.length);
    }

    @Test void mixedArrayRoundtrip() {
        // 三种不同类型在一个数组里：标量、ARRAY、STRUCTURE
        CmsData scalar  = CmsData.of(INT32, 100);
        CmsData arr     = CmsData.array(CmsData.of(BOOLEAN, true), CmsData.of(BOOLEAN, false));
        CmsData struct  = CmsData.structure(CmsData.of(FLOAT64, 3.14), CmsData.of(INT32, -1));
        CmsData outer   = CmsData.array(scalar, arr, struct);

        CmsData decoded = new CmsData().decode(outer.encode());
        CmsData[] elems = decoded.elements();
        assertEquals(3, elems.length);
        assertEquals(INT32,      elems[0].choice().value());
        assertEquals(ARRAY,      elems[1].choice().value());
        assertEquals(STRUCTURE,  elems[2].choice().value());

        // 验证内容
        CmsData[] arrElems = elems[1].elements();
        assertEquals(2, arrElems.length);
        CmsData[] structElems = elems[2].elements();
        assertEquals(2, structElems.length);
    }
}
