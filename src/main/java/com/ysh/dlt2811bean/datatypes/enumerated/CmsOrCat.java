package com.ysh.dlt2811bean.datatypes.enumerated;

/**
 * DL/T 2811 originator category type (§7.5.2).
 *
 * <p>Encoded as a 4-bit constrained enumeration (ENUMERATED 0..8).
 *
 * <pre>
 * ┌─────┬──────────────────┐
 * │ Val │ Meaning          │
 * ├─────┼──────────────────┤
 * │  0  │ not-supported    │
 * │  1  │ bay-control      │
 * │  2  │ station-control  │
 * │  3  │ remote-control   │
 * │  4  │ automatic-bay    │
 * │  5  │ automatic-station│
 * │  6  │ automatic-remote │
 * │  7  │ maintenance      │
 * │  8  │ process          │
 * └─────┴──────────────────┘
 * </pre>
 *
 * <pre>
 * // Construction
 * CmsOrCat cat = new CmsOrCat();
 * CmsOrCat cat = new CmsOrCat(CmsOrCat.BAY_CONTROL);
 * CmsOrCat cat = new CmsOrCat(1); // same as BAY_CONTROL
 *
 * // Setting values
 * cat.set(CmsOrCat.REMOTE_CONTROL);
 * cat.set(3); // same as REMOTE_CONTROL
 *
 * // Checking values
 * if (cat.is(CmsOrCat.BAY_CONTROL)) { ... }
 * if (cat.is(1)) { ... } // same as BAY_CONTROL
 *
 * // Getting the value
 * int value = cat.get(); // returns 0..8
 *
 * // Encoding and decoding
 * PerOutputStream pos = new PerOutputStream();
 * cat.encode(pos);
 *
 * PerInputStream pis = new PerInputStream(encodedData);
 * CmsOrCat decoded = new CmsOrCat().decode(pis);
 * </pre>
 *
 * <p>This is a concrete application type with fixed enumeration values.
 * The size is fixed to 9 (values 0..8).
 */
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

public class CmsOrCat extends AbstractCmsEnumerated<CmsOrCat> {

    public static final int NOT_SUPPORTED = 0;
    public static final int BAY_CONTROL = 1;
    public static final int STATION_CONTROL = 2;
    public static final int REMOTE_CONTROL = 3;
    public static final int AUTOMATIC_BAY = 4;
    public static final int AUTOMATIC_STATION = 5;
    public static final int AUTOMATIC_REMOTE = 6;
    public static final int MAINTENANCE = 7;
    public static final int PROCESS = 8;

    /**
     * Constructs a CmsOrCat with default value NOT_SUPPORTED (0).
     */
    public CmsOrCat() {
        this(NOT_SUPPORTED);
    }

    public CmsOrCat(int value) {
        super("CmsOrCat", value, 9);
    }

    private static final CmsOrCat SHARED = new CmsOrCat();

    /** Static write with raw value. */
    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    /** Static decode: creates a new instance, decodes, and returns it. */
    public static CmsOrCat read(PerInputStream pis) throws Exception {
        return new CmsOrCat().decode(pis);
    }
}