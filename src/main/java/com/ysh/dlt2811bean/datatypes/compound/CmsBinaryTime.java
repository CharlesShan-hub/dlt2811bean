package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DL/T 2811 BinaryTime type (§7.2.2 / §7.3.9 EntryTime).
 *
 * <pre>
 * ┌──────────────────┬───────────┬────────┬───────────┐
 * │ Field            │ 2811 Type │ Bits   │ Java type │
 * ├──────────────────┼───────────┼────────┼───────────┤
 * │ msOfDay          │ INT32U    │ 32     │ long      │
 * │ daysSince1984    │ INT16U    │ 16     │ int       │
 * └──────────────────┴───────────┴────────┴───────────┘
 * </pre>
 *
 * <p>Total: 48 bits (6 bytes). msOfDay: milliseconds since most recent midnight.
 * daysSince1984: days since 1984-01-01 GMT.
 *
 * <pre>
 * // Chain usage
 * CmsBinaryTime t = new CmsBinaryTime()
 *     .msOfDay(new CmsInt32U(43200000L))
 *     .daysSince1984(new CmsInt16U(15000));
 *
 * // Quick mode
 * CmsBinaryTime t = new CmsBinaryTime(43200000L, 15000);
 *
 * // Encode / Decode
 * t.encode(pos);
 * CmsBinaryTime r = new CmsBinaryTime().decode(pis);
 * </pre>
 */
@Setter
@Accessors(fluent = true)
public class CmsBinaryTime extends AbstractCmsCompound<CmsBinaryTime> {

    public CmsInt32U msOfDay = new CmsInt32U(0L);
    public CmsInt16U daysSince1984 = new CmsInt16U(0);

    public CmsBinaryTime() {
        super("BinaryTime");
        registerField("msOfDay");
        registerField("daysSince1984");
    }

    public CmsBinaryTime(long msOfDay, int daysSince1984) {
        this();
        this.msOfDay.set(msOfDay);
        this.daysSince1984.set(daysSince1984);
    }

    @Override
    protected void validate() {
        long ms = msOfDay.get();
        if (ms < 0 || ms > 86399999) {
            throw new IllegalArgumentException("msOfDay out of range (0..86399999): " + ms);
        }
        // daysSince1984 is validated by CmsInt16U itself (0..65535)
    }
}