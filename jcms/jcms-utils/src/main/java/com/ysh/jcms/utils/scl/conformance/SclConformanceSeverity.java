package com.ysh.jcms.utils.scl.conformance;

/**
 * Severity of a single Q/GDW 1396 conformance finding.
 */
public enum SclConformanceSeverity {

    /** Hard "shall (应/不应/必须)" violation - the model is not conformant. */
    ERROR,

    /** Soft "should (宜)" deviation - typical values or recommendations. */
    WARN,

    /** Informative observation (e.g. 资料性 appendix suggestions). */
    INFO
}
