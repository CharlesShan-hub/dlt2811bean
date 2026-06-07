package com.ysh.jcms.datatype.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsCheck")
class CmsCheckTest {

    @Test
    void roundtripDefault() {
        CmsCheck original = new CmsCheck();
        assertEquals(original, new CmsCheck().decode(original.encode()));
    }

    @Test
    void syncheckTrue() {
        CmsCheck original = new CmsCheck();
        original.syncheck().value(true);
        assertEquals(original, new CmsCheck().decode(original.encode()));
    }

    @Test
    void interlockCheckTrue() {
        CmsCheck original = new CmsCheck();
        original.interlock_check().value(true);
        assertEquals(original, new CmsCheck().decode(original.encode()));
    }

    @Test
    void bothTrue() {
        CmsCheck original = new CmsCheck();
        original.syncheck().value(true);
        original.interlock_check().value(true);
        assertEquals(original, new CmsCheck().decode(original.encode()));
    }

    @Test
    void decodeOverwrites() {
        CmsCheck target = new CmsCheck();
        target.syncheck().value(true);
        target.interlock_check().value(true);

        CmsCheck source = new CmsCheck();
        source.syncheck().value(false);
        source.interlock_check().value(false);

        target.decode(source.encode());
        assertFalse(target.syncheck().value());
        assertFalse(target.interlock_check().value());
    }
}
