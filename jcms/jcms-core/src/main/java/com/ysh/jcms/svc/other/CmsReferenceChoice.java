package com.ysh.jcms.svc.other;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsObjectName;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.enumerated.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * ReferenceChoice ::= CHOICE {
 *     ldName        [0] IMPLICIT ObjectName,
 *     lnReference   [1] IMPLICIT ObjectReference
 * }  —  8.3.3
 *
 * Used by GetLogicalNodeDirectory, GetAllDataValues,
 * GetAllDataDefinition, GetAllCBValues.
 */
public class CmsReferenceChoice extends CmsType {

    public static final int LD_NAME      = 0;
    public static final int LN_REFERENCE = 1;

    public CmsEnumerated       choice;           /* 0=ldName, 1=lnReference */
    public CmsObjectName       altLdName;
    public CmsObjectReference  altLnReference;

    public CmsReferenceChoice() {
        this.choice         = new CmsEnumerated();
        this.altLdName      = new CmsObjectName();
        this.altLnReference = new CmsObjectReference();
    }
    
    // -- chain setters --
    public CmsReferenceChoice choice(int v) { this.choice.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altLdName, altLnReference);
    }
}