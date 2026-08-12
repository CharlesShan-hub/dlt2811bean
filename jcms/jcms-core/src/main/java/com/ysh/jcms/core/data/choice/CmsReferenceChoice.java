package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.scalar.CmsObjectName;
import com.ysh.jcms.data.scalar.CmsObjectReference;

/**
 * <pre>
 * {@code
 * ReferenceChoice ::= CHOICE {
 *     ldName      [0] IMPLICIT ObjectName,
 *     lnReference [1] IMPLICIT ObjectReference
 * } — 8.3.3
 * }
 * </pre>
 *
 * <p>
 * Used by GetLogicalNodeDirectory, GetAllDataValues, GetAllDataDefinition,
 * GetAllCBValues. Each parent passes its own Inner*Reference type.
 */
public class CmsReferenceChoice extends CmsChoice {

    public static final int LD_NAME = 0;
    public static final int LN_REFERENCE = 1;

    @Choice(index = 0, name = "ldName", sync = Sync.WRAPPER, innerField = "ldName")
    public CmsObjectName altLdName;

    @Choice(index = 1, name = "lnReference", sync = Sync.WRAPPER, innerField = "lnReference")
    public CmsObjectReference altLnReference;

    /** Default constructor — no inner binding (InnerEmpty). */
    public CmsReferenceChoice() {
        super(new InnerEmpty());
    }

    /** Constructor with a specific Inner*Reference type (called by parent). */
    public CmsReferenceChoice(InnerBase inner) {
        super(inner);
    }

    public CmsReferenceChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /** Select ldName and set value in one call. */
    public CmsReferenceChoice altLdName(String v) {
        choice(LD_NAME);
        this.altLdName.value(v);
        return this;
    }

    /** Select lnReference and set value in one call. */
    public CmsReferenceChoice altLnReference(String v) {
        choice(LN_REFERENCE);
        this.altLnReference.value(v);
        return this;
    }

    public CmsReferenceChoice value(CmsReferenceChoice v) {
        switch (v.choice()) {
            case LD_NAME :
                return altLdName(v.altLdName.value());
            case LN_REFERENCE :
                return altLnReference(v.altLnReference.value());
            default :
                throw new IllegalArgumentException("Unknown ReferenceChoice choice: " + v.choice());
        }
    }

    public CmsReferenceChoice value(int ch, Object val) {
        choice(ch);
        switch (ch) {
            case LD_NAME :
                this.altLdName = val instanceof CmsObjectName ? (CmsObjectName) val : new CmsObjectName((String) val);
                break;
            case LN_REFERENCE :
                this.altLnReference = val instanceof CmsObjectReference ? (CmsObjectReference) val : new CmsObjectReference((String) val);
                break;
        }
        return this;
    }
}
