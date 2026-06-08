package com.ysh.jcms.datatype.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsCheck")
class CmsCheckTest {

    private CmsCheck get() { return (CmsCheck)(new CmsCheck().test()); }

    @Test
    void roundtripDefault() {
        assertEquals(get(), get().decode(get().encode()));
    }

    @Test
    void syncheckTrue() {
        CmsCheck original = get();
        original.syncheck().value(true);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void interlockCheckTrue() {
        CmsCheck original = get();
        original.interlock_check().value(true);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void bothTrue() {
        CmsCheck original = get();
        original.syncheck().value(true);
        original.interlock_check().value(true);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void decodeOverwrites() {
        CmsCheck src = get();
        src.syncheck().value(false);
        src.interlock_check().value(false);

        CmsCheck target = get();
        target.syncheck().value(true);
        target.interlock_check().value(true);
        target.decode(src.encode());
        assertFalse(target.syncheck().value());
        assertFalse(target.interlock_check().value());
    }
}
