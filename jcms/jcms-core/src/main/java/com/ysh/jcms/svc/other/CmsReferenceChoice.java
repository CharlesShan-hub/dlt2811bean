package com.ysh.jcms.svc.other;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.CmsEnumerated;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.common.CmsObjectName;
import com.ysh.jcms.data.common.CmsObjectReference;
import java.lang.reflect.Field;

/**
 * ReferenceChoice ::= CHOICE { 
 *      ldName [0] IMPLICIT ObjectName, 
 *      lnReference [1] IMPLICIT ObjectReference 
 * } — 8.3.3
 *
 * Used by GetLogicalNodeDirectory, GetAllDataValues, GetAllDataDefinition,
 * GetAllCBValues.
 *
 * Field container; encode/decode is handled by the parent PDU class.
 */
public class CmsReferenceChoice extends CmsType {

    public static final int LD_NAME = 0;
    public static final int LN_REFERENCE = 1;

    public CmsEnumerated choice; /* 0=ldName, 1=lnReference */
    public CmsObjectName altLdName;
    public CmsObjectReference altLnReference;

    public CmsReferenceChoice() {
        this.choice = new CmsEnumerated();
        this.altLdName = new CmsObjectName();
        this.altLnReference = new CmsObjectReference();
    }

    public CmsReferenceChoice choice(int v) {
        this.choice.value(v);
        return this;
    }

    /**
     * Set choice and value in one call.
     * <pre>{@code
     * reference.value(CmsReferenceChoice.LD_NAME, "ld1");
     * reference.value(CmsReferenceChoice.LN_REFERENCE, "lnRef");
     * }</pre>
     */
    public CmsReferenceChoice value(int ch, Object val) {
        this.choice.value(ch);
        switch (ch) {
            case LD_NAME:
                this.altLdName = val instanceof CmsObjectName
                    ? (CmsObjectName) val
                    : new CmsObjectName((String) val);
                break;
            case LN_REFERENCE:
                this.altLnReference = val instanceof CmsObjectReference
                    ? (CmsObjectReference) val
                    : new CmsObjectReference((String) val);
                break;
        }
        return this;
    }

    @Override
    public void syncToInner() {
        try {
            Field choiceField = inner.getClass().getField("_choice");
            Field ldNameField = inner.getClass().getField("ldName");
            Field lnRefField = inner.getClass().getField("lnReference");
            int ch = choice.value();
            if (ch == LD_NAME && altLdName != null) {
                choiceField.set(inner, "ldName");
                ldNameField.set(inner, altLdName.inner);
            } else if (altLnReference != null) {
                choiceField.set(inner, "lnReference");
                lnRefField.set(inner, altLnReference.inner);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void syncFromInner() {
        try {
            Field choiceField = inner.getClass().getField("_choice");
            Field ldNameField = inner.getClass().getField("ldName");
            Field lnRefField = inner.getClass().getField("lnReference");
            String _choice = (String) choiceField.get(inner);
            if ("ldName".equals(_choice)) {
                choice.value(LD_NAME);
                altLdName.inner = (InnerBase) ldNameField.get(inner);
            } else {
                choice.value(LN_REFERENCE);
                altLnReference.inner = (InnerBase) lnRefField.get(inner);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
