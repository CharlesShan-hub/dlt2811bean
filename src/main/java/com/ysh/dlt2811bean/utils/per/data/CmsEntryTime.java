package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * DL/T 2811 EntryTime type (§7.3.9).
 *
 * <p>EntryTime is semantically identical to {@link CmsBinaryTime} — same encoding,
 * same fields, same constraints. This class exists purely as a type alias for
 * API clarity where the standard distinguishes EntryTime from other uses of BinaryTime.
 *
 * <pre>
 * CmsEntryTime t = new CmsEntryTime(43200000, 15000);
 * CmsEntryTime.encode(pos, t);
 * CmsEntryTime r = CmsEntryTime.decode(pis);
 * </pre>
 *
 * @see CmsBinaryTime
 */
public final class CmsEntryTime extends CmsBinaryTime {

    public CmsEntryTime(int msOfDay, int daysSince1984) {
        super(msOfDay, daysSince1984);
    }

    /**
     * Encodes an EntryTime (delegates to {@link CmsBinaryTime#encode}).
     */
    public static void encode(PerOutputStream pos, CmsEntryTime value) {
        CmsBinaryTime.encode(pos, value);
    }

    /**
     * Decodes an EntryTime (delegates to {@link CmsBinaryTime#decode}, then casts).
     */
    public static CmsEntryTime decode(PerInputStream pis) throws PerDecodeException {
        CmsBinaryTime bt = CmsBinaryTime.decode(pis);
        return new CmsEntryTime(bt.getMsOfDay(), bt.getDaysSince1984());
    }
}
