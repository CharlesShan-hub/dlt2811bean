package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsEnumerated;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * ACSI class for directory services (Table 26).
 *
 * <p>Encoded as ENUMERATED (0..10).
 *
 * <pre>
 * ┌──────┬──────────────────────────────────────┐
 * │ Code │ Meaning                              │
 * ├──────┼──────────────────────────────────────┤
 * │   0  │ reserved                             │
 * │   1  │ DataObject                           │
 * │   2  │ DATA-SET                             │
 * │   3  │ BRCB                                 │
 * │   4  │ URCB                                 │
 * │   5  │ LCB                                  │
 * │   6  │ LOG                                  │
 * │   7  │ SGCB                                 │
 * │   8  │ GoCB                                 │
 * │  10  │ MSVCB                                │
 * └──────┴──────────────────────────────────────┘
 * </pre>
 */
public class CmsACSIClass extends AbstractCmsEnumerated<CmsACSIClass> {

    public static final int RESERVED = 0;
    public static final int DATA_OBJECT = 1;
    public static final int DATA_SET = 2;
    public static final int BRCB = 3;
    public static final int URCB = 4;
    public static final int LCB = 5;
    public static final int LOG = 6;
    public static final int SGCB = 7;
    public static final int GO_CB = 8;
    public static final int MSV_CB = 10;

    public CmsACSIClass() {
        this(RESERVED);
    }

    public CmsACSIClass(int value) {
        super("ACSIClass", value, 11);
    }

    private static final CmsACSIClass SHARED = new CmsACSIClass();

    public static void write(PerOutputStream pos, int value) {
        SHARED.set(value);
        SHARED.encode(pos);
    }

    public static CmsACSIClass read(PerInputStream pis) throws Exception {
        return new CmsACSIClass().decode(pis);
    }
}
