package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * DataDefResultEntry ::= SEQUENCE {
 *     cdcType       [0] IMPLICIT VisibleString OPTIONAL,
 *     definition    [1] IMPLICIT DataDefinition
 * }
 *
 * Used by GetDataDefinition Response (SEQUENCE OF DataDefResultEntry).
 */
public class CmsDataDefResultEntry extends CmsType {

    public CmsBoolean        cdcTypePresent;
    public CmsUint8Array     cdcType;        /* VisibleString OPTIONAL */
    public CmsDataDefinition definition;

    public CmsDataDefResultEntry() {
        this.cdcTypePresent = new CmsBoolean();
        this.cdcType        = new CmsUint8Array();
        this.definition     = new CmsDataDefinition();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(cdcTypePresent, cdcType, definition);
    }
}
