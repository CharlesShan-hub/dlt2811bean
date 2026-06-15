package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsEntryId;
import com.ysh.jcms.data.common.CmsEntryTime;
import java.util.Arrays;
import java.util.List;

/**
 * LogStatusValue ::= SEQUENCE {
 *     oldEntrTm   [0] IMPLICIT EntryTime,
 *     newEntrTm   [1] IMPLICIT EntryTime,
 *     oldEntr     [2] IMPLICIT EntryID,
 *     newEntr     [3] IMPLICIT EntryID
 * }  —  8.8.6
 *
 * Used by GetLogStatusValues response.
 */
public class CmsLogStatusValue extends CmsType {

    public CmsEntryTime oldEntrTm;
    public CmsEntryTime newEntrTm;
    public CmsEntryId   oldEntr;
    public CmsEntryId   newEntr;

    public CmsLogStatusValue() {
        this.oldEntrTm = new CmsEntryTime();
        this.newEntrTm = new CmsEntryTime();
        this.oldEntr   = new CmsEntryId();
        this.newEntr   = new CmsEntryId();
    }
    
    public CmsLogStatusValue oldEntrTm(CmsEntryTime v) { this.oldEntrTm = v; return this; }
    public CmsLogStatusValue newEntrTm(CmsEntryTime v) { this.newEntrTm = v; return this; }
    public CmsLogStatusValue oldEntr(byte[] v) { this.oldEntr.value(v); return this; }
    public CmsLogStatusValue oldEntr(String v) { this.oldEntr.value(v); return this; }
    public CmsLogStatusValue newEntr(byte[] v) { this.newEntr.value(v); return this; }
    public CmsLogStatusValue newEntr(String v) { this.newEntr.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(oldEntrTm, newEntrTm, oldEntr, newEntr);
    }
}