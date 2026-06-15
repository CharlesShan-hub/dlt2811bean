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
 * │   1  │ logical-device [only can use this]   │
 * │   2  │ file-system  (only for markup)       │
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
        super("ObjectClass", value, 2); // file-system is not supportted any more
        if (value != LOGICAL_DEVICE) {
            if (value == FILE_SYSTEM) {
                throw new IllegalArgumentException(
                    "ObjectClass value 2 (file-system) is not supported by GetServerDirectory, use GetFileDirectory service instead");
            }else if(value == RESERVED){
                throw new IllegalArgumentException(
                "ObjectClass value " + value + " is reserved, only logical-device(1) is allowed");
            }
        }
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
