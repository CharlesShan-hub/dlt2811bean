package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;

/**
 * DL/T 2811 additional cause for control operations (§7.5.4, Table 15).
 *
 * <p>Encoded as ENUMERATED (0..27).
 *
 * <pre>
 * ┌──────┬──────────────────────────────────────┐
 * │ Code │ Meaning                              │
 * ├──────┼──────────────────────────────────────┤
 * │   0  │ unknown                              │
 * │   1  │ not-supported                        │
 * │   2  │ blocked-by-switching-hierarchy       │
 * │   3  │ select-failed                        │
 * │   4  │ invalid-position                     │
 * │   5  │ position-reached                     │
 * │   6  │ parameter-change-in-execution        │
 * │   7  │ step-limit                           │
 * │   8  │ blocked-by-mode                      │
 * │   9  │ blocked-by-process                   │
 * │  10  │ blocked-by-interlocking              │
 * │  11  │ blocked-by-synchrocheck              │
 * │  12  │ command-already-in-execution         │
 * │  13  │ blocked-by-health                    │
 * │  14  │ 1-of-n-control                       │
 * │  15  │ abortion-by-cancel                   │
 * │  16  │ time-limit-over                      │
 * │  17  │ abortion-by-trip                     │
 * │  18  │ object-not-selected                  │
 * │  19  │ object-already-selected              │
 * │  20  │ no-access-authority                  │
 * │  21  │ ended-with-overshoot                 │
 * │  22  │ abortion-due-to-deviation            │
 * │  23  │ abortion-by-communication-loss       │
 * │  24  │ blocked-by-command                   │
 * │  25  │ none                                 │
 * │  26  │ locked-by-other-client               │
 * │  27  │ inconsistent-parameters              │
 * └──────┴──────────────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construct
 * CmsAddCause cause = new CmsAddCause();
 * cause.setCode(CmsAddCause.BLOCKED_BY_INTERLOCKING);
 *
 * // Check
 * cause.is(CmsAddCause.BLOCKED_BY_INTERLOCKING); // true
 *
 * // Encode / Decode
 * CmsAddCause.encode(pos, cause);
 * CmsAddCause r = CmsAddCause.decode(pis);
 * </pre>
 */
public final class CmsAddCause {

    // ==================== Constants (ENUMERATED 0..27) ====================

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
    public static final int BLOCKED_BY_SYNCHROCHECK = 11;
    public static final int COMMAND_ALREADY_IN_EXECUTION = 12;
    public static final int BLOCKED_BY_HEALTH = 13;
    public static final int ONE_OF_N_CONTROL = 14;
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

    private static final int MAX_VALUE = 27;

    @Getter
    private int code;

    public CmsAddCause() {
        this.code = UNKNOWN;
    }

    public CmsAddCause(int code) {
        if (code < 0 || code > MAX_VALUE) {
            throw new IllegalArgumentException("AddCause code out of range (0..27): " + code);
        }
        this.code = code;
    }

    // ==================== Semantic setter ====================

    public CmsAddCause setCode(int value) {
        if (value < 0 || value > MAX_VALUE) {
            throw new IllegalArgumentException("AddCause code out of range (0..27): " + value);
        }
        this.code = value;
        return this;
    }

    // ==================== Semantic query ====================

    /**
     * Check if code matches the given value.
     * Use with constants: {@code cause.is(CmsAddCause.BLOCKED_BY_MODE)}
     */
    public boolean is(int value) {
        if (value < 0 || value > MAX_VALUE) throw new IllegalArgumentException("AddCause code out of range (0..27): " + value);
        return code == value;
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsAddCause value) {
        CmsEnumerated.encode(pos, value.code, MAX_VALUE);
    }

    public static void encode(PerOutputStream pos, int code) {
        CmsEnumerated.encode(pos, code, MAX_VALUE);
    }

    public static CmsAddCause decode(PerInputStream pis) throws PerDecodeException {
        return new CmsAddCause(CmsEnumerated.decode(pis, MAX_VALUE).getValue());
    }

    @Override
    public String toString() {
        String name;
        switch (code) {
            case UNKNOWN: name = "unknown"; break;
            case NOT_SUPPORTED: name = "not-supported"; break;
            case BLOCKED_BY_SWITCHING_HIERARCHY: name = "blocked-by-switching-hierarchy"; break;
            case SELECT_FAILED: name = "select-failed"; break;
            case INVALID_POSITION: name = "invalid-position"; break;
            case POSITION_REACHED: name = "position-reached"; break;
            case PARAMETER_CHANGE_IN_EXECUTION: name = "parameter-change-in-execution"; break;
            case STEP_LIMIT: name = "step-limit"; break;
            case BLOCKED_BY_MODE: name = "blocked-by-mode"; break;
            case BLOCKED_BY_PROCESS: name = "blocked-by-process"; break;
            case BLOCKED_BY_INTERLOCKING: name = "blocked-by-interlocking"; break;
            case BLOCKED_BY_SYNCHROCHECK: name = "blocked-by-synchrocheck"; break;
            case COMMAND_ALREADY_IN_EXECUTION: name = "command-already-in-execution"; break;
            case BLOCKED_BY_HEALTH: name = "blocked-by-health"; break;
            case ONE_OF_N_CONTROL: name = "1-of-n-control"; break;
            case ABORTION_BY_CANCEL: name = "abortion-by-cancel"; break;
            case TIME_LIMIT_OVER: name = "time-limit-over"; break;
            case ABORTION_BY_TRIP: name = "abortion-by-trip"; break;
            case OBJECT_NOT_SELECTED: name = "object-not-selected"; break;
            case OBJECT_ALREADY_SELECTED: name = "object-already-selected"; break;
            case NO_ACCESS_AUTHORITY: name = "no-access-authority"; break;
            case ENDED_WITH_OVERSHOOT: name = "ended-with-overshoot"; break;
            case ABORTION_DUE_TO_DEVIATION: name = "abortion-due-to-deviation"; break;
            case ABORTION_BY_COMMUNICATION_LOSS: name = "abortion-by-communication-loss"; break;
            case BLOCKED_BY_COMMAND: name = "blocked-by-command"; break;
            case NONE: name = "none"; break;
            case LOCKED_BY_OTHER_CLIENT: name = "locked-by-other-client"; break;
            case INCONSISTENT_PARAMETERS: name = "inconsistent-parameters"; break;
            default: name = "unknown-" + code;
        }
        return String.format("AddCause[%d=%s]", code, name);
    }
}
