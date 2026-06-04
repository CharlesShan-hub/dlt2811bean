package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;

import java.util.Arrays;
import java.util.List;

/**
 * cms_lcb_t — LCB (Log Control Block).
 *
 * C: typedef struct {
 *     int         logEna;
 *     char        datSet[256];
 *     uint8_t     trgOps[1];
 *     uint32_t    intgPd;
 *     char        logRef[256];
 *     uint8_t     optFlds[1];
 *     int         optFlds_present;
 *     uint32_t    bufTm;
 *     int         bufTm_present;
 * } cms_lcb_t;
 */
public class CmsLcb extends Structure {
    public CmsBoolean logEna = new CmsBoolean();
    public byte[] datSet = new byte[256];
    public byte[] trgOps = new byte[1];
    public CmsInt32U intgPd = new CmsInt32U();
    public byte[] logRef = new byte[256];
    public byte[] optFlds = new byte[1];
    public CmsBoolean optFldsPresent = new CmsBoolean();
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsBoolean bufTmPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("logEna", "datSet", "trgOps", "intgPd", "logRef",
                "optFlds", "optFldsPresent", "bufTm", "bufTmPresent");
    }
}
