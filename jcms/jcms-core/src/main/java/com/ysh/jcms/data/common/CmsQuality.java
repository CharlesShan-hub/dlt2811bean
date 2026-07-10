package com.ysh.jcms.data.common;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.*;
import java.util.Arrays;
import java.util.List;

/**
 * Quality ::= BIT STRING (SIZE(13)) — 7.3.6
 */
public class CmsQuality extends CmsType {

    public CmsInt32 validity;
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
        super(Codec.QUALITY);
        this.validity = new CmsInt32();
        this.overflow = new CmsBoolean();
        this.outOfRange = new CmsBoolean();
        this.badReference = new CmsBoolean();
        this.oscillatory = new CmsBoolean();
        this.failure = new CmsBoolean();
        this.oldData = new CmsBoolean();
        this.inconsistent = new CmsBoolean();
        this.inaccurate = new CmsBoolean();
        this.substituted = new CmsBoolean();
        this.test = new CmsBoolean();
        this.operatorBlocked = new CmsBoolean();
    }

    public CmsQuality validity(int v) {
        this.validity.value(v);
        return this;
    }
    public CmsQuality overflow(boolean v) {
        this.overflow.value(v);
        return this;
    }
    public CmsQuality outOfRange(boolean v) {
        this.outOfRange.value(v);
        return this;
    }
    public CmsQuality badReference(boolean v) {
        this.badReference.value(v);
        return this;
    }
    public CmsQuality oscillatory(boolean v) {
        this.oscillatory.value(v);
        return this;
    }
    public CmsQuality failure(boolean v) {
        this.failure.value(v);
        return this;
    }
    public CmsQuality oldData(boolean v) {
        this.oldData.value(v);
        return this;
    }
    public CmsQuality inconsistent(boolean v) {
        this.inconsistent.value(v);
        return this;
    }
    public CmsQuality inaccurate(boolean v) {
        this.inaccurate.value(v);
        return this;
    }
    public CmsQuality substituted(boolean v) {
        this.substituted.value(v);
        return this;
    }
    public CmsQuality test(boolean v) {
        this.test.value(v);
        return this;
    }
    public CmsQuality operatorBlocked(boolean v) {
        this.operatorBlocked.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(validity, overflow, outOfRange, badReference, oscillatory, failure, oldData, inconsistent, inaccurate,
                substituted, test, operatorBlocked);
    }
}
