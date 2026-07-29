package com.ysh.jcms.data.sequence.common;

/**
 * TimeStamp ::= UtcTime — 7.3.4 PER encoding: same as UtcTime (OCTET STRING
 * (SIZE(8))).
 *
 * Use {@link CmsUtcTime} directly — this subclass exists only for
 * ASN.1 type-name documentation.
 */
public class CmsTimeStamp extends CmsUtcTime {
    /** @deprecated Use {@link CmsUtcTime} instead. */
    @Deprecated
    public CmsTimeStamp() {
        throw new UnsupportedOperationException("Use CmsUtcTime directly");
    }
}
