package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsSubReference;
import java.util.Arrays;
import java.util.List;

/**
 * DataValueEntry ::= SEQUENCE {
 *     reference     [0] IMPLICIT SubReference,
 *     value         [1] IMPLICIT Data
 * }  —  8.3.4
 *
 * Used by GetAllDataValues response (SEQUENCE OF DataValueEntry).
 */
public class CmsDataValueEntry extends CmsType {

    public CmsSubReference reference;
    public CmsData         value;

    public CmsDataValueEntry() {
        this.reference = new CmsSubReference();
        this.value     = new CmsData();
    }
    
    // -- chain setters --
    public CmsDataValueEntry reference(byte[] v) { this.reference.value(v); return this; }
    public CmsDataValueEntry reference(String v) { this.reference.value(v); return this; }
    public CmsDataValueEntry value(CmsData v) { this.value = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reference, value);
    }
}