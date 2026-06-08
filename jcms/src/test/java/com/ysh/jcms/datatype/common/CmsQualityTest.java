package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQuality")
class CmsQualityTest {

    private CmsQuality get() {
        return (CmsQuality)(new CmsQuality()).super_test();
    }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void validity() {
        CmsQuality q = get();
        q.validity().value(1);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void overflow() {
        CmsQuality q = get();
        q.overflow().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void outOfRange() {
        CmsQuality q = get();
        q.outOfRange().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void test() {
        CmsQuality q = get();
        q.test().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void multipleFlags() {
        CmsQuality q = get();
        q.overflow().value(true);
        q.failure().value(true);
        q.operatorBlocked().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void allBooleansTrue() {
        CmsQuality q = get();
        q.overflow().value(true);
        q.outOfRange().value(true);
        q.badReference().value(true);
        q.oscillatory().value(true);
        q.failure().value(true);
        q.oldData().value(true);
        q.inconsistent().value(true);
        q.inaccurate().value(true);
        q.substituted().value(true);
        q.test().value(true);
        q.operatorBlocked().value(true);
        assertEquals(q, get().decode(q.encode()));
    }

    @Test
    void allBooleansFalse() {
        assertEquals(get(), get().decode(get().encode()));
    }
}
