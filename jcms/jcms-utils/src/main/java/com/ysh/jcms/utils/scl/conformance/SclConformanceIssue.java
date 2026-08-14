package com.ysh.jcms.utils.scl.conformance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * One Q/GDW 1396 conformance finding.
 * <p>
 * Carries the affected SCL reference (e.g. {@code E1Q1SB1/C1}), the standard
 * clause it violates (e.g. {@code 7.1.3}), and a human-readable message. The
 * category groups findings by rule family (LD-NAMING, COMM-PARAM, ...).
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclConformanceIssue {

    /** Severity: ERROR / WARN / INFO. */
    private SclConformanceSeverity severity;

    /** Rule family, e.g. "LD-NAMING", "COMM-PARAM", "STRUCTURE". */
    private String category;

    /** Q/GDW 1396 clause, e.g. "7.1.3" or "6.5.2". */
    private String clause;

    /** Affected element reference, e.g. "E1Q1SB1/C1". */
    private String ref;

    /** Human-readable description of the deviation. */
    private String message;
}
