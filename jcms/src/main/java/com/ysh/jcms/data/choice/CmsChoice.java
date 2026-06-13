package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.enumerated.CmsEnumerated;

/**
 * Base class for CHOICE types.
 *
 * Only provides the `choice` selector field. Subclasses manage their
 * own full all-pointer layout matching the C struct.
 */
public abstract class CmsChoice {

    public CmsEnumerated choice;

    protected CmsChoice() {
        this.choice = new CmsEnumerated();
    }
}
