package com.ysh.jcms.data.sequence.common;

/**
 * <pre>
 * {@code
 * EntryTime ::= BinaryTime — 7.3.9
 * }
 * </pre>
 *
 * <p>
 * PER encoding: same as BinaryTime (OCTET STRING (SIZE(6))).
 *
 * <p>
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
