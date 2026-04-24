package com.ysh.dlt2811bean.utils.per.data;

/**
 * DL/T 2811 EntryTime type (§7.3.9).
 *
 * <p>EntryTime is semantically identical to {@link CmsBinaryTime} — same encoding,
 * same fields, same constraints. This class exists purely as a type alias for
 * API clarity where the standard distinguishes EntryTime from other uses of BinaryTime.
 *
 * <pre>
 * // Chain usage
 * CmsEntryTime t = new CmsEntryTime()
 *     .msOfDay(new CmsInt32U(43200000L))
 *     .daysSince1984(new CmsInt16U(15000));
 *
 * // Quick mode
 * CmsEntryTime t = new CmsEntryTime(43200000L, 15000);
 *
 * // Encode / Decode
 * t.encode(pos);
 * CmsEntryTime r = new CmsEntryTime().decode(pis);
 * </pre>
 *
 * @see CmsBinaryTime
 */
public class CmsEntryTime extends CmsBinaryTime {

    public CmsEntryTime() {
        this(0L, 0);
    }

    /**
     * @deprecated Use {@link CmsBinaryTime} directly instead.
     * CmsEntryTime is an alias kept for backward compatibility.
     */
    @Deprecated(forRemoval = true)
    public CmsEntryTime(long msOfDay, int daysSince1984) {
        super(msOfDay, daysSince1984);
    }
}
