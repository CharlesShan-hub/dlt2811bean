package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.core.data.sequence.block.CmsSgcb;

/**
 * Unified facade for control block runtime state.
 * <p>
 * Standard 7.6.1 defines six control blocks: BRCB, URCB, LCB, SGCB, GoCB,
 * MSVCB. Lifecycle layering (corresponding to the {@code CbFieldScope} of
 * {@code @CbField}):
 * <ul>
 * <li><b>ENGINEERING</b> — no storage, reads the SCL model directly (read-only
 * base)</li>
 * <li><b>RUNTIME</b> — {@link #RCB}/{@link #LCB}/{@link #GOCB}/{@link #MSVCB},
 * effective in-process</li>
 * <li><b>ASSOCIATION</b> — {@link #ASSOCIATION}, isolated by session, cleared
 * when the connection is closed</li>
 * </ul>
 * SGCB is not in this list: its state (actSG/editSG/edit buffer) is
 * session-level, managed by SgSessionState in jcms-app, and the field lifecycle
 * is already annotated with {@code @CbField} on CmsSgcb.
 *
 * Runtime SGCB entries are stored in {@link #SGCB} and initialized at server
 * startup from the numOfSG config; per-session state (actSG/editSG) is overlaid
 * via SgSessionState.
 */
public final class CbStateManager {

    private CbStateManager() {
    }

    /** RCB runtime state (BRCB/URCB share the CmsBrcb carrier). */
    public static final CbStateStore<CmsBrcb> RCB = new CbStateStore<>();
    /** LCB runtime state. */
    public static final CbStateStore<CmsLcb> LCB = new CbStateStore<>();
    /** GoCB runtime state. */
    public static final CbStateStore<CmsGoCb> GOCB = new CbStateStore<>();
    /** MSVCB runtime state. */
    public static final CbStateStore<CmsMsvcb> MSVCB = new CbStateStore<>();
    /**
     * SGCB runtime entries, initialized at server startup from the numOfSG config.
     */
    public static final CbStateStore<CmsSgcb> SGCB = new CbStateStore<>();

    /** Association-level state (reserved for URCB per-association fields). */
    public static final CbAssociationStore<CmsBrcb> ASSOCIATION = new CbAssociationStore<>();

    /** Clears all association-level state of the session on association release. */
    public static void clearAssociation(String sessionId) {
        ASSOCIATION.removeSession(sessionId);
    }

    /** Clears all runtime state (server stop/restart). */
    public static void clearAll() {
        RCB.clear();
        LCB.clear();
        GOCB.clear();
        MSVCB.clear();
        SGCB.clear();
        ASSOCIATION.clear();
    }
}
