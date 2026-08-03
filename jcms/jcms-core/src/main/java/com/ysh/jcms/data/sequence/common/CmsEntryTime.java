package com.ysh.jcms.data.sequence.common;

/**
 * EntryTime ::= BinaryTime — 7.3.9 PER encoding: same as BinaryTime (OCTET
 * STRING (SIZE(6))).
 *
 * Use {@link CmsBinaryTime} directly — this subclass exists only for ASN.1
 * type-name documentation.
 */
public class CmsEntryTime extends CmsBinaryTime {
    /** @deprecated Use {@link CmsBinaryTime} instead. */
    @Deprecated
    public CmsEntryTime() {
        throw new UnsupportedOperationException("Use CmsBinaryTime directly");
    }
}
