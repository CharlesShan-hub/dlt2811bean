package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.InnerField;
import com.ysh.jcms.data.common.CmsEntryId;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsBrcbTest {
    @Test
    public void roundup() {
        CmsBrcb a = new CmsBrcb().rptID("rpt01").rptEna(true).datSet("dataset1").confRev(3L)
                .optFlds(new CmsRcbOptFlds().sequence_number(true)).bufTm(5000L).sqNum(100)
                .trgOps(new CmsTriggerConditions().data_change(true)).intgPd(3000L).gi(false).purgeBuf(true)
                .entryID(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        byte[] encoded = a.encode();

        CmsBrcb b = new CmsBrcb();
        b.decode(encoded);

        // ── Debug: compare each @InnerField wrapper ────────────────
        System.out.println("=== @InnerField wrapper comparison ===");
        boolean allOk = true;
        for (Field f : CmsBrcb.class.getFields()) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            InnerField ann = f.getAnnotation(InnerField.class);
            if (ann == null) continue;
            try {
                Object va = f.get(a);
                Object vb = f.get(b);
                boolean eq = (va == vb) || (va != null && va.equals(vb));
                if (!eq) {
                    allOk = false;
                    System.out.println("  FAIL: " + f.getName()
                        + " (optional=" + ann.optional() + ")");
                    if (va instanceof CmsType) {
                        Object valA = ((CmsType)va).innerCache.get("value");
                        Object valB = ((CmsType)vb).innerCache.get("value");
                        System.out.println("    a.innerCache[\"value\"]=" + valA);
                        System.out.println("    b.innerCache[\"value\"]=" + valB);
                        // Also print full innerCache
                        System.out.println("    a.innerCache=" + ((CmsType)va).innerCache);
                        System.out.println("    b.innerCache=" + ((CmsType)vb).innerCache);
                    } else {
                        System.out.println("    a=" + va);
                        System.out.println("    b=" + vb);
                    }
                }
            } catch (Exception e) {
                System.out.println("  ERROR accessing " + f.getName() + ": " + e);
            }
        }
        if (allOk) System.out.println("  All @InnerField wrappers equal!");

        // ── Also compare top-level innerCache ─────────────────────
        System.out.println("\n=== Top-level innerCache comparison ===");
        System.out.println("a.innerCache=" + a.innerCache);
        System.out.println("b.innerCache=" + b.innerCache);
        System.out.println("a.innerCache.equals(b.innerCache)=" + a.innerCache.equals(b.innerCache));

        // Compare entry by entry
        for (Map.Entry<String, Object> e : a.innerCache.entrySet()) {
            Object va = e.getValue();
            Object vb = b.innerCache.get(e.getKey());
            boolean eq = (va == vb) || (va != null && va.equals(vb));
            if (!eq) {
                System.out.println("  DIFF key='" + e.getKey() + "': a=" + va + " b=" + vb);
            }
        }
        // Check keys in b but not in a
        for (String k : b.innerCache.keySet()) {
            if (!a.innerCache.containsKey(k)) {
                System.out.println("  EXTRA key in b: '" + k + "' = " + b.innerCache.get(k));
            }
        }
        // Check keys in a but not in b
        for (String k : a.innerCache.keySet()) {
            if (!b.innerCache.containsKey(k)) {
                System.out.println("  EXTRA key in a: '" + k + "' = " + a.innerCache.get(k));
            }
        }

        System.out.println("\n=== Direct equals checks ===");
        System.out.println("entryID.equals: " + a.entryID.equals(b.entryID));
        // Direct CmsScalar test
        CmsEntryId e1 = new CmsEntryId(); e1.value(new byte[]{1,2,3,4,5,6,7,8});
        CmsEntryId e2 = new CmsEntryId(); e2.value(new byte[]{1,2,3,4,5,6,7,8});
        System.out.println("  fresh entryID.equals: " + e1.equals(e2));
        System.out.println("  fresh Arrays.equals: " + java.util.Arrays.equals(e1.value(), e2.value()));
        // Check CmsType super.equals
        System.out.println("  CmsType.super.equals: " + ((com.ysh.jcms.core.CmsType)a.entryID).equals((com.ysh.jcms.core.CmsType)b.entryID));
        // List all public fields
        System.out.println("  CmsEntryId public fields:");
        for (Field ff : CmsEntryId.class.getFields()) {
            System.out.println("    " + ff.getName() + " (" + ff.getType().getSimpleName() + ", static=" + Modifier.isStatic(ff.getModifiers()) + ")");
            try {
                Object ffva = ff.get(a.entryID);
                Object ffvb = ff.get(b.entryID);
                System.out.println("      a=" + ffva + " b=" + ffvb + " equal=" + (ffva == ffvb || (ffva != null && ffva.equals(ffvb))));
            } catch (Exception e) {
                System.out.println("      ERROR: " + e);
            }
        }
        // Check if getClass matches
        System.out.println("  a.entryID.class=" + a.entryID.getClass());
        System.out.println("  b.entryID.class=" + b.entryID.getClass());
        System.out.println("  a.entryID.class == b.entryID.class: " + (a.entryID.getClass() == b.entryID.getClass()));
        // Check innerCache values manually
        Object va = a.entryID.innerCache.get("value");
        Object vb = b.entryID.innerCache.get("value");
        System.out.println("  manual: va=" + va + " vb=" + vb);
        System.out.println("  va.getClass=" + va.getClass() + " vb.getClass=" + vb.getClass());
        System.out.println("  va instanceof byte[]: " + (va instanceof byte[]));
        System.out.println("  vb instanceof byte[]: " + (vb instanceof byte[]));
        System.out.println("  Arrays.equals manual: " + java.util.Arrays.equals((byte[])va, (byte[])vb));
        System.out.println("owner.equals: " + a.owner.equals(b.owner));
        System.out.println("timeOfEntry.equals: " + a.timeOfEntry.equals(b.timeOfEntry));
        System.out.println("rptID.equals: " + a.rptID.equals(b.rptID));

        assertEquals(a, b);
    }
}
