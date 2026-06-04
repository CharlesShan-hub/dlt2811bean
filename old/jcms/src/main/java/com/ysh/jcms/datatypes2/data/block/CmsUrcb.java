package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;

import java.util.Arrays;
import java.util.List;

/**
 * cms_urcb_t — URCB (Unbuffered Report Control Block).
 *
 * C: typedef struct {
 *     char        rptID[130];
 *     int         rptEna;
 *     char        datSet[256];
 *     uint32_t    confRev;
 *     uint8_t     optFlds[2];
 *     uint32_t    bufTm;
 *     uint16_t    sqNum;
 *     uint8_t     trgOps[1];
 *     uint32_t    intgPd;
 *     int         gi;
 *     int         resv;
 *     uint8_t     owner[64];
 *     int         owner_len;
 *     int         owner_present;
 * } cms_urcb_t;
 */
public class CmsUrcb extends Structure {
    public byte[] rptID = new byte[130];
    public CmsBoolean rptEna = new CmsBoolean();
    public byte[] datSet = new byte[256];
    public CmsInt32U confRev = new CmsInt32U();
    public byte[] optFlds = new byte[2];
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsInt16U sqNum = new CmsInt16U();
    public byte[] trgOps = new byte[1];
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsBoolean gi = new CmsBoolean();
    public CmsBoolean resv = new CmsBoolean();
    public byte[] owner = new byte[64];
    public int ownerLen;
    public CmsBoolean ownerPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("rptID", "rptEna", "datSet", "confRev", "optFlds", "bufTm",
                "sqNum", "trgOps", "intgPd", "gi", "resv", "owner", "ownerLen", "ownerPresent");
    }
}
