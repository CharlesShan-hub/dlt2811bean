package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 控制块运行时状态分层存储测试（RUNTIME / ASSOCIATION / 门面）。
 */
public class CbStateStoreTest {

    // ==================== RUNTIME 层（CbStateStore） ====================

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
        assertSame(a, b); // 已存在则复用
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

    // ==================== ASSOCIATION 层（CbAssociationStore） ====================

    @Test
    public void testAssociationIsolationBySession() {
        CbAssociationStore<CmsBrcb> store = new CbAssociationStore<>();
        store.put("s1", "ref", new CmsBrcb());
        store.put("s2", "ref", new CmsBrcb());
        // 不同会话互不干扰
        assertNotNull(store.get("s1", "ref"));
        assertNotNull(store.get("s2", "ref"));
        // 会话隔离：修改 s2 不影响 s1
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
        assertNotNull(store.get("s2", "ref")); // 其他会话不受影响
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

    // ==================== 门面（CbStateManager） ====================

    @Test
    public void testStateManagerLifecycle() {
        CbStateManager.RCB.put("C1/LLN0.PosReport", new CmsBrcb());
        CbStateManager.GOCB.put("C1/LLN0.ItlPositions", new CmsGoCb());
        CbStateManager.ASSOCIATION.put("session-1", "C1/LLN0.PosReport", new CmsBrcb());

        assertNotNull(CbStateManager.RCB.get("C1/LLN0.PosReport"));
        assertNotNull(CbStateManager.GOCB.get("C1/LLN0.ItlPositions"));
        assertNotNull(CbStateManager.ASSOCIATION.get("session-1", "C1/LLN0.PosReport"));

        // 关联释放只清该会话
        CbStateManager.clearAssociation("session-1");
        assertNull(CbStateManager.ASSOCIATION.get("session-1", "C1/LLN0.PosReport"));
        assertNotNull(CbStateManager.RCB.get("C1/LLN0.PosReport")); // RUNTIME 不受影响

        // 服务器停止清全部
        CbStateManager.clearAll();
        assertNull(CbStateManager.RCB.get("C1/LLN0.PosReport"));
        assertNull(CbStateManager.GOCB.get("C1/LLN0.ItlPositions"));
    }
}
