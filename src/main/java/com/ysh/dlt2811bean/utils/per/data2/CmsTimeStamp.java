package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 TimeStamp type (§7.3.4).
 *
 * <p>TimeStamp is semantically identical to {@link CmsUtcTime} — same encoding,
 * same fields, same constraints. This class exists purely as a type alias for
 * API clarity where the standard distinguishes TimeStamp from other uses of UtcTime.
 *
 * <pre>
 * CmsTimeStamp ts = new CmsTimeStamp(1715000000L, 0, 0x20);
 * CmsTimeStamp.encode(pos, ts);
 * CmsTimeStamp r = CmsTimeStamp.decode(pis);
 * </pre>
 *
 * @see CmsUtcTime
 */
public final class CmsTimeStamp extends CmsUtcTime {

    public CmsTimeStamp() {
        super();
    }

    public CmsTimeStamp(long secondsSinceEpoch, int fractionOfSecond, int rawTimeQuality) {
        super(secondsSinceEpoch, fractionOfSecond, rawTimeQuality);
    }

    public CmsTimeStamp(long secondsSinceEpoch, int fractionOfSecond, CmsTimeQuality timeQuality) {
        super(secondsSinceEpoch, fractionOfSecond, timeQuality);
    }

    /**
     * Encodes a TimeStamp (delegates to {@link CmsUtcTime#encode}).
     */
    public static void encode(PerOutputStream pos, CmsTimeStamp value) {
        CmsUtcTime.encode(pos, value);
    }

    /**
     * Decodes a TimeStamp (delegates to {@link CmsUtcTime#decode}, then casts).
     */
    public static CmsTimeStamp decode(PerInputStream pis) throws PerDecodeException {
        CmsUtcTime utc = CmsUtcTime.decode(pis);
        return new CmsTimeStamp(
                utc.getSecondsSinceEpoch(),
                utc.getFractionOfSecond(),
                utc.getTimeQuality().toRaw()
        );
    }
}
