package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;

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
 * // Create
 * CmsServiceError err = new CmsServiceError(CmsServiceError.NO_ERROR);
 * err.setAccessViolation();
 *
 * // Check
 * if (err.isNoError()) { ... }
 * String name = err.getValue(); // 3
 *
 * // Encode / Decode
 * CmsServiceError.encode(pos, err);
 * CmsServiceError r = CmsServiceError.decode(pis);
 * </pre>
 */
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

    @Getter
    private int value;

    public CmsServiceError() {
        this.value = NO_ERROR;
    }

    public CmsServiceError(int value) {
        if (value < 0 || value > 12) {
            throw new IllegalArgumentException("value out of range (0..12)");
        }
        this.value = value;
    }

    // ==================== Semantic setters ====================

    public CmsServiceError setNoError() { this.value = NO_ERROR; return this; }
    public CmsServiceError setInstanceNotAvailable() { this.value = INSTANCE_NOT_AVAILABLE; return this; }
    public CmsServiceError setInstanceInUse() { this.value = INSTANCE_IN_USE; return this; }
    public CmsServiceError setAccessViolation() { this.value = ACCESS_VIOLATION; return this; }
    public CmsServiceError setAccessNotAllowedInCurrentState() { this.value = ACCESS_NOT_ALLOWED_IN_CURRENT_STATE; return this; }
    public CmsServiceError setParameterValueInappropriate() { this.value = PARAMETER_VALUE_INAPPROPRIATE; return this; }
    public CmsServiceError setParameterValueInconsistent() { this.value = PARAMETER_VALUE_INCONSISTENT; return this; }
    public CmsServiceError setClassNotSupported() { this.value = CLASS_NOT_SUPPORTED; return this; }
    public CmsServiceError setInstanceLockedByOtherClient() { this.value = INSTANCE_LOCKED_BY_OTHER_CLIENT; return this; }
    public CmsServiceError setControlMustBeSelected() { this.value = CONTROL_MUST_BE_SELECTED; return this; }
    public CmsServiceError setTypeConflict() { this.value = TYPE_CONFLICT; return this; }
    public CmsServiceError setFailedDueToCommunicationsConstraint() { this.value = FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT; return this; }
    public CmsServiceError setFailedDueToServerConstraint() { this.value = FAILED_DUE_TO_SERVER_CONSTRAINT; return this; }

    // ==================== Semantic getters ====================

    public boolean isNoError() { return value == NO_ERROR; }
    public boolean isInstanceNotAvailable() { return value == INSTANCE_NOT_AVAILABLE; }
    public boolean isInstanceInUse() { return value == INSTANCE_IN_USE; }
    public boolean isAccessViolation() { return value == ACCESS_VIOLATION; }
    public boolean isAccessNotAllowedInCurrentState() { return value == ACCESS_NOT_ALLOWED_IN_CURRENT_STATE; }
    public boolean isParameterValueInappropriate() { return value == PARAMETER_VALUE_INAPPROPRIATE; }
    public boolean isParameterValueInconsistent() { return value == PARAMETER_VALUE_INCONSISTENT; }
    public boolean isClassNotSupported() { return value == CLASS_NOT_SUPPORTED; }
    public boolean isInstanceLockedByOtherClient() { return value == INSTANCE_LOCKED_BY_OTHER_CLIENT; }
    public boolean isControlMustBeSelected() { return value == CONTROL_MUST_BE_SELECTED; }
    public boolean isTypeConflict() { return value == TYPE_CONFLICT; }
    public boolean isFailedDueToCommunicationsConstraint() { return value == FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT; }
    public boolean isFailedDueToServerConstraint() { return value == FAILED_DUE_TO_SERVER_CONSTRAINT; }

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsServiceError value) {
        encode(pos, value.value);
    }

    public static void encode(PerOutputStream pos, int value) {
        CmsEnumerated.encode(pos, value, 12);
    }

    public static CmsServiceError decode(PerInputStream pis) throws PerDecodeException {
        int raw = (int) CmsEnumerated.decode(pis, 12).getValue();
        return new CmsServiceError(raw);
    }

    @Override
    public String toString() {
        return "ServiceError[" + value + "]";
    }
}
