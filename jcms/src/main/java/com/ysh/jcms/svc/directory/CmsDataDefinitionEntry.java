package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.util.Arrays;
import java.util.List;

/**
 * DataDefinitionEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     cdcType       [1] IMPLICIT VisibleString OPTIONAL,
 *     definition    [2] IMPLICIT DataDefinition
 * }  —  8.3.5
 *
 * Used by GetAllDataDefinition response (SEQUENCE OF DataDefinitionEntry).
 */
public class CmsDataDefinitionEntry extends CmsType {

    public CmsSubReference    reference;
    public CmsBoolean         cdcTypePresent;
    public CmsUint8Array      cdcType;        /* VisibleString OPTIONAL */
    public CmsDataDefinition  definition;

    public CmsDataDefinitionEntry() {
        this.reference      = new CmsSubReference();
        this.cdcTypePresent = new CmsBoolean();
        this.cdcType        = new CmsUint8Array();
        this.definition     = new CmsDataDefinition();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, cdcTypePresent, cdcType, definition);
    }
}
