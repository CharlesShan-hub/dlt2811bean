package com.ysh.jcms.data.sequence.goose;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.scalar.CmsString;

/**
 * SetGoCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     goEna       [1] IMPLICIT Boolean OPTIONAL,
 *     goID        [2] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL
 * } — 8.9.5
 *
 * <p>Used by SetGoCBValues request.
 */
public class CmsSetGoCbEntry extends CmsSequence {

    @CmsField
    public CmsObjectReference reference;

    @CmsField(optional = true)
    public CmsBoolean goEna;

    @CmsField(optional = true)
    public CmsString goID;

    @CmsField(optional = true)
    public CmsObjectReference datSet;

    public CmsSetGoCbEntry() {
        super(new InnerEmpty());
        this.reference = new CmsObjectReference();
        this.goEna = new CmsBoolean();
        this.goID = new CmsString();
        this.datSet = new CmsObjectReference();
    }

    public CmsSetGoCbEntry reference(String v) { this.reference.value(v); return this; }
    public CmsSetGoCbEntry reference(byte[] v) { return reference(new String(v, StandardCharsets.UTF_8)); }
    public CmsSetGoCbEntry goEna(boolean v) {
        this.goEna.value(v);
        setPresent("goEna", true);
        return this;
    }
    public CmsSetGoCbEntry goID(String v) {
        if (v != null) {
            this.goID.value(v);
            setPresent("goID", true);
        } else {
            setPresent("goID", false);
        }
        return this;
    }
    public CmsSetGoCbEntry goID(byte[] v) { return goID(v != null ? new String(v, StandardCharsets.UTF_8) : null); }
    public CmsSetGoCbEntry datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSetGoCbEntry datSet(byte[] v) { return datSet(v != null ? new String(v, StandardCharsets.UTF_8) : null); }

    public CmsSetGoCbEntry value(CmsSetGoCbEntry v) {
        reference(v.reference.value());
        if (v.isPresent("goEna")) { this.goEna.value(v.goEna.value()); setPresent("goEna", true); }
        else { setPresent("goEna", false); }
        if (v.isPresent("goID")) { this.goID.value(v.goID.value()); setPresent("goID", true); }
        else { setPresent("goID", false); }
        if (v.isPresent("datSet")) { this.datSet.value(v.datSet.value()); setPresent("datSet", true); }
        else { setPresent("datSet", false); }
        return this;
    }
}
