package com.ysh.dlt2811bean.datatypes.enumerated;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * DL/T 2811 service error type (§7.3.11, Table 12).
 *
 * <p>Encoded as a 4-bit constrained enumeration (ENUMERATED 0..12).
 *
 * <pre>
 * ┌─────┬─────────────────────────────────────────┐
 * │ Code│ Meaning                                 │
 * ├─────┼─────────────────────────────────────────┤
 * │  0  │ no-error                                │
 * │  1  │ instance-not-available                  │
 * │  2  │ instance-in-use                         │
 * │  3  │ access-violation                        │
 * │  4  │ access-not-allowed-in-current-state     │
 * │  5  │ parameter-value-inappropriate           │
 * │  6  │ parameter-value-inconsistent            │
 * │  7  │ class-not-supported                     │
 * │  8  │ instance-locked-by-other-client         │
 * │  9  │ control-must-be-selected                │
 * │ 10  │ type-conflict                           │
 * │ 11  │ failed-due-to-communications-constraint │
 * │ 12  │ failed-due-to-server-constraint         │
 * └─────┴─────────────────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction
 * CmsServiceError err = new CmsServiceError();
 * CmsServiceError err = new CmsServiceError(CmsServiceError.ACCESS_VIOLATION);
 * CmsServiceError err = new CmsServiceError(3); // same as ACCESS_VIOLATION
 *
 * // Setting values
 * err.set(CmsServiceError.NO_ERROR);
 * err.set(0); // same as NO_ERROR
 *
 * // Checking values
 * if (err.is(CmsServiceError.ACCESS_VIOLATION)) { ... }
 * if (err.is(3)) { ... } // same as ACCESS_VIOLATION
 *
 * // Getting the value
 * int value = err.get(); // returns 0..12
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * err.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsServiceError decoded = new CmsServiceError().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with fixed enumeration values.
 * The size is fixed to 13 (values 0..12).
 */
public class CmsServiceError extends AbstractCmsEnumerated<CmsServiceError> {

    public static final int NO_ERROR = 0;
    public static final int INSTANCE_NOT_AVAILABLE = 1;
    public static final int INSTANCE_IN_USE = 2;
    public static final int ACCESS_VIOLATION = 3;
    public static final int ACCESS_NOT_ALLOWED_IN_CURRENT_STATE = 4;
    public static final int PARAMETER_VALUE_INAPPROPRIATE = 5;
    public static final int PARAMETER_VALUE_INCONSISTENT = 6;
    public static final int CLASS_NOT_SUPPORTED = 7;
    public static final int INSTANCE_LOCKED_BY_OTHER_CLIENT = 8;
    public static final int CONTROL_MUST_BE_SELECTED = 9;
    public static final int TYPE_CONFLICT = 10;
    public static final int FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT = 11;
    public static final int FAILED_DUE_TO_SERVER_CONSTRAINT = 12;

    /**
     * Constructs a CmsServiceError with default value NO_ERROR (0).
     */
    public CmsServiceError() {
        this(NO_ERROR);
    }

    public CmsServiceError(int value) {
        super("CmsServiceError", value, 13);
    }

    private static final CmsServiceError SHARED = new CmsServiceError();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsServiceError read(PerInputStream pis) throws Exception {
        return new CmsServiceError().decode(pis);
    }
}