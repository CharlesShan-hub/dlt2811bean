package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 service error type (§7.3.11, Table 12).
 *
 * <p>Encoded as INT8 (§7.1.6 ENUMERATED).
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
 * // Create
 * CmsServiceError err = new CmsServiceError(CmsServiceError.NO_ERROR);
 * err.setCode(CmsServiceError.ACCESS_VIOLATION);
 *
 * // Check
 * if (err.isSuccess()) { ... }
 * String name = err.getCodeName(); // "access-violation"
 *
 * // Encode / Decode
 * CmsServiceError.encode(pos, err);
 * CmsServiceError r = CmsServiceError.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsServiceError {

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

    private int code;

    public CmsServiceError() {
        this.code = NO_ERROR;
    }

    public CmsServiceError(int code) {
        if (code < 0 || code > 127) {
            throw new IllegalArgumentException("code out of INT8 range");
        }
        this.code = code;
    }

    /** Returns true if code == NO_ERROR (0). */
    public boolean isSuccess() {
        return code == NO_ERROR;
    }

    /** Returns true if code != NO_ERROR. */
    public boolean isError() {
        return code != NO_ERROR;
    }

    /** Returns the symbolic name for known codes, or "unknown-" + code. */
    public String getCodeName() {
        switch (code) {
            case NO_ERROR: return "no-error";
            case INSTANCE_NOT_AVAILABLE: return "instance-not-available";
            case INSTANCE_IN_USE: return "instance-in-use";
            case ACCESS_VIOLATION: return "access-violation";
            case ACCESS_NOT_ALLOWED_IN_CURRENT_STATE: return "access-not-allowed-in-current-state";
            case PARAMETER_VALUE_INAPPROPRIATE: return "parameter-value-inappropriate";
            case PARAMETER_VALUE_INCONSISTENT: return "parameter-value-inconsistent";
            case CLASS_NOT_SUPPORTED: return "class-not-supported";
            case INSTANCE_LOCKED_BY_OTHER_CLIENT: return "instance-locked-by-other-client";
            case CONTROL_MUST_BE_SELECTED: return "control-must-be-selected";
            case TYPE_CONFLICT: return "type-conflict";
            case FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT: return "failed-due-to-communications-constraint";
            case FAILED_DUE_TO_SERVER_CONSTRAINT: return "failed-due-to-server-constraint";
            default: return "unknown-" + code;
        }
    }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsServiceError value) {
        PerInteger.encode(pos, value.code, -128, 127);
    }

    public static CmsServiceError decode(PerInputStream pis) throws PerDecodeException {
        return new CmsServiceError((int) PerInteger.decode(pis, -128, 127));
    }

    @Override
    public String toString() {
        return String.format("ServiceError[%d=%s]", code, getCodeName());
    }
}
