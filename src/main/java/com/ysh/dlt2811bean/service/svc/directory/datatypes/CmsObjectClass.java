package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * Object class for the GetServerDirectory service (Table 23).
 *
 * <p>Encoded as a constrained integer 0..2:
 * <ul>
 *   <li>0 = reserved</li>
 *   <li>1 = logical-device (list logical devices)</li>
 *   <li>2 = file-system (list file system roots)</li>
 * </ul>
 *
 * <pre>
 * ┌──────┬──────────────────────────────────────┐
 * │ Code │ Meaning                              │
 * ├──────┼──────────────────────────────────────┤
 * │   0  │ reserved                             │
 * │   1  │ logical-device                       │
 * │   2  │ file-system                          │
 * └──────┴──────────────────────────────────────┘
 * </pre>
 */
public class CmsObjectClass extends AbstractCmsEnumerated<CmsObjectClass> {

    public static final int RESERVED = 0;
    public static final int LOGICAL_DEVICE = 1;
    public static final int FILE_SYSTEM = 2;

    public CmsObjectClass() {
        this(LOGICAL_DEVICE);
    }

    public CmsObjectClass(int value) {
        super("ObjectClass", value, 3);
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
