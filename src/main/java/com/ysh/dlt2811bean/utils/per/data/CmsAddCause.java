package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

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
 * // Construction
 * CmsAddCause cause = new CmsAddCause();
 * CmsAddCause cause = new CmsAddCause(CmsAddCause.BLOCKED_BY_INTERLOCKING);
 * CmsAddCause cause = new CmsAddCause(10); // same as BLOCKED_BY_INTERLOCKING
 *
 * // Setting values
 * cause.set(CmsAddCause.NOT_SUPPORTED);
 * cause.set(1); // same as NOT_SUPPORTED
 *
 * // Checking values
 * if (cause.is(CmsAddCause.BLOCKED_BY_INTERLOCKING)) { ... }
 * if (cause.is(10)) { ... } // same as BLOCKED_BY_INTERLOCKING
 *
 * // Getting the value
 * int value = cause.get(); // returns 0..27
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * cause.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsAddCause decoded = new CmsAddCause().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with fixed enumeration values.
 * The size is fixed to 28 (values 0..27).
 */
public class CmsAddCause extends AbstractCmsEnumerated<CmsAddCause> {

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

    /**
     * Constructs a CmsAddCause with default value UNKNOWN (0).
     */
    public CmsAddCause() {
        this(UNKNOWN);
    }

    public CmsAddCause(int value) {
        super("CmsAddCause", value, 28);
    }

    private static final CmsAddCause SHARED = new CmsAddCause();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsAddCause read(PerInputStream pis) throws Exception {
        return new CmsAddCause().decode(pis);
    }
}