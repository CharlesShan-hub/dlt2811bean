package com.ysh.jcms.core.data.enumerate;

import com.ysh.jcms.data.core.CmsEnum;
import com.ysh.jcms.data.InnerAddCause;

/**
 * <pre>
 * {@code
 * AddCause ::= INTEGER {
 *     unknown                              (0),
 *     not-supported                        (1),
 *     blocked-by-switching-hierarchy       (2),
 *     select-failed                        (3),
 *     invalid-position                     (4),
 *     position-reached                     (5),
 *     parameter-change-in-execution        (6),
 *     step-limit                           (7),
 *     blocked-by-mode                      (8),
 *     blocked-by-process                   (9),
 *     blocked-by-interlocking              (10),
 *     blocked-by-syncheck                  (11),
 *     command-already-in-execution         (12),
 *     blocked-by-health                    (13),
 *     one-of-a-control                     (14),
 *     abortion-by-cancel                   (15),
 *     time-limit-over                      (16),
 *     abortion-by-trip                     (17),
 *     object-not-selected                  (18),
 *     object-already-selected              (19),
 *     no-access-authority                  (20),
 *     ended-with-overshoot                 (21),
 *     abortion-due-to-deviation            (22),
 *     abortion-by-communication-loss       (23),
 *     blocked-by-command                   (24),
 *     none                                 (25),
 *     locked-by-other-client               (26),
 *     inconsistent-parameters              (27)
 * } (0..27) — 7.5.4
 * }
 * </pre>
 *
 * <p>
 * PER: constrained integer (0..27), 5 bits; sizeof = 4.
 */
@CmsEnum.ValueRange(min = 0, max = 27)
public class CmsAddCause extends CmsEnum<CmsAddCause> {

    public static final int UNKNOWN = 0;
    public static final int NOT_SUPPORTED = 1;
    public static final int BLOCKED_BY_SWITCHING_HIERARCHY = 2;
    public static final int SELECT_FAILED = 3;
    public static final int INVALID_POSITION = 4;
    public static final int POSITION_REACHED = 5;
    public static final int PARAMETER_CHANGE_IN_EXECUTION = 6;
    public static final int STEP_LIMIT = 7;
    public static final int BLOCKED_BY_MODE = 8;
    public static final int BLOCKED_BY_PROCESS = 9;
    public static final int BLOCKED_BY_INTERLOCKING = 10;
    public static final int BLOCKED_BY_SYNCHECK = 11;
    public static final int COMMAND_ALREADY_IN_EXECUTION = 12;
    public static final int BLOCKED_BY_HEALTH = 13;
    public static final int ONE_OF_A_CONTROL = 14;
    public static final int ABORTION_BY_CANCEL = 15;
    public static final int TIME_LIMIT_OVER = 16;
    public static final int ABORTION_BY_TRIP = 17;
    public static final int OBJECT_NOT_SELECTED = 18;
    public static final int OBJECT_ALREADY_SELECTED = 19;
    public static final int NO_ACCESS_AUTHORITY = 20;
    public static final int ENDED_WITH_OVERSHOOT = 21;
    public static final int ABORTION_DUE_TO_DEVIATION = 22;
    public static final int ABORTION_BY_COMMUNICATION_LOSS = 23;
    public static final int BLOCKED_BY_COMMAND = 24;
    public static final int NONE = 25;
    public static final int LOCKED_BY_OTHER_CLIENT = 26;
    public static final int INCONSISTENT_PARAMETERS = 27;

    public CmsAddCause() {
        super(new InnerAddCause());
    }
    public CmsAddCause(int v) {
        this();
        value(v);
    }
}
