package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;

/**
 * 控制块运行时状态统一门面。
 * <p>
 * 标准 7.6.1 定义六种控制块：BRCB、URCB、LCB、SGCB、GoCB、MSVCB。 生命周期分层（对应 {@code @CbField} 的
 * {@code CbFieldScope}）：
 * <ul>
 * <li><b>ENGINEERING</b> — 无存储，直接读 SCL 模型（只读基底）</li>
 * <li><b>RUNTIME</b> —
 * {@link #RCB}/{@link #LCB}/{@link #GOCB}/{@link #MSVCB}，进程内生效</li>
 * <li><b>ASSOCIATION</b> — {@link #ASSOCIATION}，按会话隔离，连接断开清除</li>
 * </ul>
 * SGCB 不在此列：它的状态（actSG/editSG/编辑缓冲）是会话级，由 jcms-app 的 SgSessionState 管理，字段生命周期已用
 * {@code @CbField} 标注在 CmsSgcb 上。
 */
public final class CbStateManager {

    private CbStateManager() {
    }

    /** RCB 运行时状态（BRCB/URCB 共用 CmsBrcb 载体）。 */
    public static final CbStateStore<CmsBrcb> RCB = new CbStateStore<>();
    /** LCB 运行时状态。 */
    public static final CbStateStore<CmsLcb> LCB = new CbStateStore<>();
    /** GoCB 运行时状态。 */
    public static final CbStateStore<CmsGoCb> GOCB = new CbStateStore<>();
    /** MSVCB 运行时状态。 */
    public static final CbStateStore<CmsMsvcb> MSVCB = new CbStateStore<>();

    /** 关联级状态（URCB per-association 字段预留）。 */
    public static final CbAssociationStore<CmsBrcb> ASSOCIATION = new CbAssociationStore<>();

    /** 关联释放时清理该会话的全部关联级状态。 */
    public static void clearAssociation(String sessionId) {
        ASSOCIATION.removeSession(sessionId);
    }

    /** 清空全部运行时状态（服务器停止/重启）。 */
    public static void clearAll() {
        RCB.clear();
        LCB.clear();
        GOCB.clear();
        MSVCB.clear();
        ASSOCIATION.clear();
    }
}
