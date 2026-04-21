package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 boolean type (§7.1.1).
 *
 * <pre>
 * ┌──────────┬─────────────────┬──────┬───────────┐
 * │ 2811     │ Range           │ Bits │ Java type │
 * ├──────────┼─────────────────┼──────┼───────────┤
 * │ BOOLEAN  │ FALSE | TRUE    │ 1    │ boolean   │
 * └──────────┴─────────────────┴──────┴───────────┘
 * </pre>
 *
 * <p>Encoding: single bit, FALSE=0, TRUE=1.
 *
 * <pre>
 * // Bean usage
 * CmsBoolean val = new CmsBoolean(true);
 * val.setValue(false);
 * CmsBoolean.encode(pos, val);
 *
 * // Quick usage — pass raw boolean directly
 * CmsBoolean.encode(pos, true);
 *
 * // Decode always returns a bean
 * CmsBoolean r = CmsBoolean.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsBoolean {

    private boolean value;

    public CmsBoolean() {
        this.value = false;
    }

    public CmsBoolean(boolean value) {
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /** Encodes a CmsBoolean bean. */
    public static void encode(PerOutputStream pos, CmsBoolean val) {
        PerBoolean.encode(pos, val.value);
    }

    /** Encodes a raw boolean value. */
    public static void encode(PerOutputStream pos, boolean val) {
        PerBoolean.encode(pos, val);
    }

    public static CmsBoolean decode(PerInputStream pis) throws PerDecodeException {
        return new CmsBoolean(PerBoolean.decode(pis));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
