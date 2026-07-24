package com.ysh.jcms.data.common;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerQuality;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32;

/**
 * Quality ::= BIT STRING (SIZE(13)) — 7.3.6
 * <p>
 * Bit 0-1 = validity (2-bit: 0=good, 1=invalid, 2=reserved, 3=questionable).
 * Bit 2-12 = single-bit boolean flags.
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
        super(new InnerQuality());
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

    public CmsQuality validity(int v) { this.validity.value(v); return this; }
    public CmsQuality overflow(boolean v) { this.overflow.value(v); return this; }
    public CmsQuality outOfRange(boolean v) { this.outOfRange.value(v); return this; }
    public CmsQuality badReference(boolean v) { this.badReference.value(v); return this; }
    public CmsQuality oscillatory(boolean v) { this.oscillatory.value(v); return this; }
    public CmsQuality failure(boolean v) { this.failure.value(v); return this; }
    public CmsQuality oldData(boolean v) { this.oldData.value(v); return this; }
    public CmsQuality inconsistent(boolean v) { this.inconsistent.value(v); return this; }
    public CmsQuality inaccurate(boolean v) { this.inaccurate.value(v); return this; }
    public CmsQuality substituted(boolean v) { this.substituted.value(v); return this; }
    public CmsQuality test(boolean v) { this.test.value(v); return this; }
    public CmsQuality operatorBlocked(boolean v) { this.operatorBlocked.value(v); return this; }

    @Override
    public void syncToInner() {
        int packed = 0;
        packed |= (validity.value() & 0x03) << 0;
        if (overflow.value()) packed |= (1 << 2);
        if (outOfRange.value()) packed |= (1 << 3);
        if (badReference.value()) packed |= (1 << 4);
        if (oscillatory.value()) packed |= (1 << 5);
        if (failure.value()) packed |= (1 << 6);
        if (oldData.value()) packed |= (1 << 7);
        if (inconsistent.value()) packed |= (1 << 8);
        if (inaccurate.value()) packed |= (1 << 9);
        if (substituted.value()) packed |= (1 << 10);
        if (test.value()) packed |= (1 << 11);
        if (operatorBlocked.value()) packed |= (1 << 12);
        ((InnerQuality) inner).value = packed;
    }

    @Override
    public void syncFromInner() {
        int packed = ((InnerQuality) inner).value;
        validity.value(packed & 0x03);
        overflow.value((packed >> 2 & 1) != 0);
        outOfRange.value((packed >> 3 & 1) != 0);
        badReference.value((packed >> 4 & 1) != 0);
        oscillatory.value((packed >> 5 & 1) != 0);
        failure.value((packed >> 6 & 1) != 0);
        oldData.value((packed >> 7 & 1) != 0);
        inconsistent.value((packed >> 8 & 1) != 0);
        inaccurate.value((packed >> 9 & 1) != 0);
        substituted.value((packed >> 10 & 1) != 0);
        test.value((packed >> 11 & 1) != 0);
        operatorBlocked.value((packed >> 12 & 1) != 0);
    }
}
