package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Layered runtime state storage tests for control blocks (RUNTIME / ASSOCIATION / facade).
 */
public class CbStateStoreTest {

    // ==================== RUNTIME layer (CbStateStore) ====================

    @Test
    public void testCbStateStorePutGetRemove() {
        CbStateStore<CmsBrcb> store = new CbStateStore<>();
        assertNull(store.get("C1/LLN0.PosReport"));

        CmsBrcb brcb = new CmsBrcb();
        brcb.rptID("RC");
        store.put("C1/LLN0.PosReport", brcb);
        assertEquals("RC", store.get("C1/LLN0.PosReport").rptID.value());

        store.remove("C1/LLN0.PosReport");
        assertNull(store.get("C1/LLN0.PosReport"));
    }

    @Test
    public void testCbStateStoreGetOrCreate() {
        CbStateStore<CmsBrcb> store = new CbStateStore<>();
        CmsBrcb a = store.getOrCreate("ref1", CmsBrcb::new);
        CmsBrcb b = store.getOrCreate("ref1", CmsBrcb::new);
        assertSame(a, b); // reuse existing instance
        assertNotNull(store.getOrCreate("ref2", CmsBrcb::new));
    }

    @Test
    public void testCbStateStoreClear() {
        CbStateStore<CmsBrcb> store = new CbStateStore<>();
        store.put("a", new CmsBrcb());
        store.put("b", new CmsBrcb());
        store.clear();
        assertNull(store.get("a"));
        assertNull(store.get("b"));
    }

    // ==================== ASSOCIATION layer (CbAssociationStore) ====================

    @Test
    public void testAssociationIsolationBySession() {
        CbAssociationStore<CmsBrcb> store = new CbAssociationStore<>();
        store.put("s1", "ref", new CmsBrcb());
        store.put("s2", "ref", new CmsBrcb());
        // different sessions do not interfere with each other
        assertNotNull(store.get("s1", "ref"));
        assertNotNull(store.get("s2", "ref"));
        // session isolation: modifying s2 does not affect s1
        String s1RptId = store.get("s1", "ref").rptID.value();
        store.get("s2", "ref").rptID("changed");
        assertEquals(s1RptId, store.get("s1", "ref").rptID.value());
        assertEquals("changed", store.get("s2", "ref").rptID.value());
    }

    @Test
    public void testAssociationRemoveSession() {
        CbAssociationStore<CmsBrcb> store = new CbAssociationStore<>();
        store.put("s1", "ref", new CmsBrcb());
        store.put("s2", "ref", new CmsBrcb());
        store.removeSession("s1");
        assertNull(store.get("s1", "ref"));
        assertNotNull(store.get("s2", "ref")); // other sessions unaffected
    }

    @Test
    public void testAssociationRemoveAndClear() {
        CbAssociationStore<CmsBrcb> store = new CbAssociationStore<>();
        store.put("s1", "ref1", new CmsBrcb());
        store.put("s1", "ref2", new CmsBrcb());
        store.remove("s1", "ref1");
        assertNull(store.get("s1", "ref1"));
        assertNotNull(store.get("s1", "ref2"));
        store.clear();
        assertNull(store.get("s1", "ref2"));
    }

    // ==================== facade (CbStateManager) ====================

    @Test
    public void testStateManagerLifecycle() {
        CbStateManager.RCB.put("C1/LLN0.PosReport", new CmsBrcb());
        CbStateManager.GOCB.put("C1/LLN0.ItlPositions", new CmsGoCb());
        CbStateManager.ASSOCIATION.put("session-1", "C1/LLN0.PosReport", new CmsBrcb());

        assertNotNull(CbStateManager.RCB.get("C1/LLN0.PosReport"));
        assertNotNull(CbStateManager.GOCB.get("C1/LLN0.ItlPositions"));
        assertNotNull(CbStateManager.ASSOCIATION.get("session-1", "C1/LLN0.PosReport"));

        // releasing the association only clears that session
        CbStateManager.clearAssociation("session-1");
        assertNull(CbStateManager.ASSOCIATION.get("session-1", "C1/LLN0.PosReport"));
        assertNotNull(CbStateManager.RCB.get("C1/LLN0.PosReport")); // RUNTIME unaffected

        // server shutdown clears everything
        CbStateManager.clearAll();
        assertNull(CbStateManager.RCB.get("C1/LLN0.PosReport"));
        assertNull(CbStateManager.GOCB.get("C1/LLN0.ItlPositions"));
    }
}
