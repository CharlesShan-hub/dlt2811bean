package com.ysh.jcms.pdu;

import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.core.CmsType;
import java.util.List;
import org.junit.Assert;

/**
 * Custom assertions for Cms types. Compares Cms-layer data only, avoiding
 * Inner* field-by-field comparison that fails on null vs default-initialized
 * variant fields after roundtrip (e.g. InnerData unselected variants).
 */
public class CmsAssert {

    /**
     * Assert two CmsSequence objects are equal by syncing and comparing
     * their inner objects. Falls back to field-by-field comparison for
     * List fields that contain nested CmsType elements.
     */
    public static void assertSeqEquals(CmsSequence expected, CmsSequence actual) {
        if (expected == actual) return;
        Assert.assertNotNull("expected is null", expected);
        Assert.assertNotNull("actual is null", actual);
        Assert.assertEquals("class mismatch", expected.getClass(), actual.getClass());

        // Sync both sides first
        expected.syncToInner();
        actual.syncToInner();

        // Compare CmsType fields via reflection
        try {
            for (java.lang.reflect.Field f : expected.getClass().getFields()) {
                Object ev = f.get(expected);
                Object av = f.get(actual);
                if (!compareField(ev, av)) {
                    Assert.assertEquals("field '" + f.getName() + "' mismatch",
                            ev instanceof CmsType ? ((CmsType) ev).toString() : String.valueOf(ev),
                            av instanceof CmsType ? ((CmsType) av).toString() : String.valueOf(av));
                    return;
                }
            }
        } catch (IllegalAccessException e) {
            Assert.fail("reflection error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean compareField(Object ev, Object av) {
        if (ev == av) return true;
        if (ev == null || av == null) return false;

        // CmsType fields — use their own equals (syncs + compares inner)
        if (ev instanceof CmsType && av instanceof CmsType) {
            return ev.equals(av);
        }

        // List of CmsType (SEQUENCE OF) — compare element by element
        if (ev instanceof List && av instanceof List) {
            List<Object> el = (List<Object>) ev;
            List<Object> al = (List<Object>) av;
            if (el.size() != al.size()) return false;
            for (int i = 0; i < el.size(); i++) {
                if (!compareField(el.get(i), al.get(i))) return false;
            }
            return true;
        }

        // Fallback — standard equals
        return ev.equals(av);
    }
}
