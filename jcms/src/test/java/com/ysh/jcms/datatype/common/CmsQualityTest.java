package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQuality")
class CmsQualityTest {

    @Test
    void roundtripDefault() {
        assertEquals(new CmsQuality(), new CmsQuality().decode(new CmsQuality().encode()));
    }

    @Test
    void validity() {
        CmsQuality q = new CmsQuality();
        q.validity().value(1);
        assertEquals(q, new CmsQuality().decode(q.encode()));
    }

    @Test
    void overflow() {
        CmsQuality q = new CmsQuality();
        q.overflow().value(true);
        assertEquals(q, new CmsQuality().decode(q.encode()));
    }

    @Test
    void outOfRange() {
        CmsQuality q = new CmsQuality();
        q.outOfRange().value(true);
        assertEquals(q, new CmsQuality().decode(q.encode()));
    }

    @Test
    void test() {
        CmsQuality q = new CmsQuality();
        q.test().value(true);
        assertEquals(q, new CmsQuality().decode(q.encode()));
    }

    @Test
    void multipleFlags() {
        CmsQuality q = new CmsQuality();
        q.overflow().value(true);
        q.failure().value(true);
        q.operatorBlocked().value(true);
        assertEquals(q, new CmsQuality().decode(q.encode()));
    }

    @Test
    void allBooleansTrue() {
        CmsQuality q = new CmsQuality();
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
        assertEquals(q, new CmsQuality().decode(q.encode()));
    }

    @Test
    void allBooleansFalse() {
        CmsQuality q = new CmsQuality();
        // all defaults are false, just roundtrip
        assertEquals(q, new CmsQuality().decode(q.encode()));
    }
}
