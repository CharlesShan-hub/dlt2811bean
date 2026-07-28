package com.ysh.jcms.data.string;

import com.ysh.jcms.core.CmsScalar;
import com.ysh.jcms.data.DefaultInnerVisibleString;

/**
 * Generic VisibleString wrapper, backed by DefaultInnerVisibleString.
 * Sync to the real Inner* field is handled by the parent CmsSequence
 * via @InnerField injection.
 */
public class CmsString extends CmsScalar {

    public CmsString() { super(new DefaultInnerVisibleString()); }
    public CmsString(String v) { this(); value(v); }

    public String value() { return (String) innerGet(); }
    public CmsString value(String v) { innerSet(v); return this; }
}
