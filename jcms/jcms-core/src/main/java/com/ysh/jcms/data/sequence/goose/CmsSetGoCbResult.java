package com.ysh.jcms.data.sequence.goose;

import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * SetGoCBResult ::= SEQUENCE {
 *     error   [0] IMPLICIT ServiceError OPTIONAL,
 *     goEna   [1] IMPLICIT ServiceError OPTIONAL,
 *     goID    [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet  [3] IMPLICIT ServiceError OPTIONAL
 * } — 8.9.5
 *
 * <p>Used by SetGoCBValues error.
 */
public class CmsSetGoCbResult extends CmsSequence {

    @CmsField(optional = true)
    public CmsServiceError error;

    @CmsField(optional = true)
    public CmsServiceError goEna;

    @CmsField(optional = true)
    public CmsServiceError goID;

    @CmsField(optional = true)
    public CmsServiceError datSet;

    public CmsSetGoCbResult() {
        super(new InnerEmpty());
        this.error = new CmsServiceError();
        this.goEna = new CmsServiceError();
        this.goID = new CmsServiceError();
        this.datSet = new CmsServiceError();
    }

    public CmsSetGoCbResult error(int v) { this.error.value(v); setPresent("error", true); return this; }
    public CmsSetGoCbResult goEna(int v) { this.goEna.value(v); setPresent("goEna", true); return this; }
    public CmsSetGoCbResult goID(int v) { this.goID.value(v); setPresent("goID", true); return this; }
    public CmsSetGoCbResult datSet(int v) { this.datSet.value(v); setPresent("datSet", true); return this; }

    public CmsSetGoCbResult value(CmsSetGoCbResult v) {
        if (v.isPresent("error")) { this.error.value(v.error.value()); setPresent("error", true); }
        else { setPresent("error", false); }
        if (v.isPresent("goEna")) { this.goEna.value(v.goEna.value()); setPresent("goEna", true); }
        else { setPresent("goEna", false); }
        if (v.isPresent("goID")) { this.goID.value(v.goID.value()); setPresent("goID", true); }
        else { setPresent("goID", false); }
        if (v.isPresent("datSet")) { this.datSet.value(v.datSet.value()); setPresent("datSet", true); }
        else { setPresent("datSet", false); }
        return this;
    }
}
