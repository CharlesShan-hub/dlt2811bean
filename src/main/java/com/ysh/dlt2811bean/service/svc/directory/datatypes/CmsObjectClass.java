package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * Object class for the GetServerDirectory service (Table 23).
 *
 * <p>Encoded as ENUMERATED (0..1).
 *
 * <pre>
 * ┌──────┬──────────────────────────────────────┐
 * │ Code │ Meaning                              │
 * ├──────┼──────────────────────────────────────┤
 * │   0  │ reserved                             │
 * │   1  │ logical-device                       │
 * └──────┴──────────────────────────────────────┘
 * </pre>
 */
public class CmsObjectClass extends AbstractCmsEnumerated<CmsObjectClass> {

    public static final int RESERVED = 0;
    public static final int LOGICAL_DEVICE = 1;

    public CmsObjectClass() {
        this(RESERVED);
    }

    public CmsObjectClass(int value) {
        super("ObjectClass", value, 2);
    }

    private static final CmsObjectClass SHARED = new CmsObjectClass();

    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    public static CmsObjectClass read(PerInputStream pis) throws Exception {
        return new CmsObjectClass().decode(pis);
    }
}
