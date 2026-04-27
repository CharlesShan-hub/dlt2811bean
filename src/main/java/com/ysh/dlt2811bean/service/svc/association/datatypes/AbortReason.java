package com.ysh.dlt2811bean.service.svc.association.datatypes;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * Abort reason for the Abort service (§8.2.3, Table 21).
 *
 * <p>Encoded as ENUMERATED (0..5).
 *
 * <pre>
 * ┌──────┬──────────────────────────────────────┐
 * │ Code │ Meaning                              │
 * ├──────┼──────────────────────────────────────┤
 * │   0  │ other                                │
 * │   1  │ unrecognized-service                 │
 * │   2  │ invalid-reqID                        │
 * │   3  │ invalid-argument                     │
 * │   4  │ invalid-result                       │
 * │   5  │ max-serv-outstanding-exceeded        │
 * └──────┴──────────────────────────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction
 * AbortReason reason = new AbortReason();
 * AbortReason reason = new AbortReason(AbortReason.OTHER);
 * AbortReason reason = new AbortReason(3); // same as INVALID_ARGUMENT
 *
 * // Setting values
 * reason.set(AbortReason.UNRECOGNIZED_SERVICE);
 * reason.set(1); // same as UNRECOGNIZED_SERVICE
 *
 * // Checking values
 * if (reason.is(AbortReason.INVALID_ARGUMENT)) { ... }
 * if (reason.is(3)) { ... } // same as INVALID_ARGUMENT
 *
 * // Getting the value
 * int value = reason.get(); // returns 0..5
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * reason.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * AbortReason decoded = new AbortReason().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with fixed enumeration values.
 * The size is fixed to 6 (values 0..5).
 */
public class AbortReason extends AbstractCmsEnumerated<AbortReason> {

    public static final int OTHER = 0;
    public static final int UNRECOGNIZED_SERVICE = 1;
    public static final int INVALID_REQ_ID = 2;
    public static final int INVALID_ARGUMENT = 3;
    public static final int INVALID_RESULT = 4;
    public static final int MAX_SERV_OUTSTANDING_EXCEEDED = 5;

    /**
     * Constructs an AbortReason with default value OTHER (0).
     */
    public AbortReason() {
        this(OTHER);
    }

    public AbortReason(int value) {
        super("AbortReason", value, 6);
    }

    private static final AbortReason SHARED = new AbortReason();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static AbortReason read(PerInputStream pis) throws Exception {
        return new AbortReason().decode(pis);
    }
}
