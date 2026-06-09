package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.*;
import java.util.Arrays;
import java.util.List;

/**
 * Quality ::= BIT STRING (SIZE(13))  —  7.3.6
 * PER: align + 2 bytes (13 bits)
 *
 * All-pointer container:
 *   validity (2 bits), overflow~operatorBlocked (11 booleans)
 */
public class CmsQuality extends CmsType {

    public CmsInt32   validity;
    public CmsBoolean overflow;
    public CmsBoolean outOfRange;
    public CmsBoolean badReference;
    public CmsBoolean oscillatory;
    public CmsBoolean failure;
    public CmsBoolean oldData;
    public CmsBoolean inconsistent;
    public CmsBoolean inaccurate;
    public CmsBoolean substituted;
    public CmsBoolean test;
    public CmsBoolean operatorBlocked;

    public CmsQuality() {
        this.validity        = new CmsInt32();
        this.overflow        = new CmsBoolean();
        this.outOfRange      = new CmsBoolean();
        this.badReference    = new CmsBoolean();
        this.oscillatory     = new CmsBoolean();
        this.failure         = new CmsBoolean();
        this.oldData         = new CmsBoolean();
        this.inconsistent    = new CmsBoolean();
        this.inaccurate      = new CmsBoolean();
        this.substituted     = new CmsBoolean();
        this.test            = new CmsBoolean();
        this.operatorBlocked = new CmsBoolean();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(validity, overflow, outOfRange, badReference,
            oscillatory, failure, oldData, inconsistent, inaccurate,
            substituted, test, operatorBlocked);
    }
}
