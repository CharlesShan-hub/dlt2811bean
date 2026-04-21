package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerEnumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 additional cause for control operations (§7.5.4, Table 15).
 *
 * <p>Encoded as ENUMERATED (§7.1.6), range 0..27.
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
 * // Create
 * CmsAddCause cause = new CmsAddCause(CmsAddCause.BLOCKED_BY_INTERLOCKING);
 * cause.setCode(CmsAddCause.TIME_LIMIT_OVER);
 *
 * // Check
 * String name = cause.getCodeName(); // "time-limit-over"
 * boolean blocked = cause.isBlocked(); // true for all "blocked-by-*" codes
 *
 * // Encode / Decode
 * CmsAddCause.encode(pos, cause);
 * CmsAddCause r = CmsAddCause.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsAddCause {

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

    /** Maximum valid code (27). */
    public static final int MAX_CODE = 27;

    private int code;

    public CmsAddCause() {
        this.code = UNKNOWN;
    }

    public CmsAddCause(int code) {
        if (code < 0 || code > MAX_CODE) {
            throw new IllegalArgumentException(
                String.format("AddCause code %d out of range [0, %d]", code, MAX_CODE));
        }
        this.code = code;
    }

    /** Returns true if code indicates a blocked state (codes 2, 8-14, 20, 24, 26). */
    public boolean isBlocked() {
        switch (code) {
            case BLOCKED_BY_SWITCHING_HIERARCHY:
            case BLOCKED_BY_MODE:
            case BLOCKED_BY_PROCESS:
            case BLOCKED_BY_INTERLOCKING:
            case BLOCKED_BY_SYNCHROCHECK:
            case BLOCKED_BY_HEALTH:
            case BLOCKED_BY_COMMAND:
            case NO_ACCESS_AUTHORITY:
            case LOCKED_BY_OTHER_CLIENT:
                return true;
            default:
                return false;
        }
    }

    /** Returns true if code indicates an aborted operation (codes 15, 17, 22, 23). */
    public boolean isAborted() {
        switch (code) {
            case ABORTION_BY_CANCEL:
            case ABORTION_BY_TRIP:
            case ABORTION_DUE_TO_DEVIATION:
            case ABORTION_BY_COMMUNICATION_LOSS:
                return true;
            default:
                return false;
        }
    }

    /** Returns the symbolic name for known codes, or "unknown-" + code. */
    public String getCodeName() {
        switch (code) {
            case UNKNOWN: return "unknown";
            case NOT_SUPPORTED: return "not-supported";
            case BLOCKED_BY_SWITCHING_HIERARCHY: return "blocked-by-switching-hierarchy";
            case SELECT_FAILED: return "select-failed";
            case INVALID_POSITION: return "invalid-position";
            case POSITION_REACHED: return "position-reached";
            case PARAMETER_CHANGE_IN_EXECUTION: return "parameter-change-in-execution";
            case STEP_LIMIT: return "step-limit";
            case BLOCKED_BY_MODE: return "blocked-by-mode";
            case BLOCKED_BY_PROCESS: return "blocked-by-process";
            case BLOCKED_BY_INTERLOCKING: return "blocked-by-interlocking";
            case BLOCKED_BY_SYNCHROCHECK: return "blocked-by-synchrocheck";
            case COMMAND_ALREADY_IN_EXECUTION: return "command-already-in-execution";
            case BLOCKED_BY_HEALTH: return "blocked-by-health";
            case ONE_OF_N_CONTROL: return "1-of-n-control";
            case ABORTION_BY_CANCEL: return "abortion-by-cancel";
            case TIME_LIMIT_OVER: return "time-limit-over";
            case ABORTION_BY_TRIP: return "abortion-by-trip";
            case OBJECT_NOT_SELECTED: return "object-not-selected";
            case OBJECT_ALREADY_SELECTED: return "object-already-selected";
            case NO_ACCESS_AUTHORITY: return "no-access-authority";
            case ENDED_WITH_OVERSHOOT: return "ended-with-overshoot";
            case ABORTION_DUE_TO_DEVIATION: return "abortion-due-to-deviation";
            case ABORTION_BY_COMMUNICATION_LOSS: return "abortion-by-communication-loss";
            case BLOCKED_BY_COMMAND: return "blocked-by-command";
            case NONE: return "none";
            case LOCKED_BY_OTHER_CLIENT: return "locked-by-other-client";
            case INCONSISTENT_PARAMETERS: return "inconsistent-parameters";
            default: return "unknown-" + code;
        }
    }

    // ==================== Encode / Decode ====================

    /**
     * Encodes AddCause as ENUMERATED (0..27).
     * <p>Range 0..27 requires ceil(log2(28)) = 5 bits in constrained PER.
     */
    public static void encode(PerOutputStream pos, CmsAddCause value) {
        PerEnumerated.encode(pos, value.code, MAX_CODE);
    }

    /**
     * Decodes AddCause from ENUMERATED (0..27).
     */
    public static CmsAddCause decode(PerInputStream pis) throws PerDecodeException {
        return new CmsAddCause(PerEnumerated.decode(pis, MAX_CODE));
    }

    @Override
    public String toString() {
        return String.format("AddCause[%d=%s]", code, getCodeName());
    }
}
