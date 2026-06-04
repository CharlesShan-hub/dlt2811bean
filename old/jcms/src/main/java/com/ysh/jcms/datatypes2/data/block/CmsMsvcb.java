package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;

import java.util.Arrays;
import java.util.List;

/**
 * cms_msvcb_t — MSVCB (Multicast Sampled Value Control Block).
 *
 * C: typedef struct {
 *     int         svEna;
 *     char        msvID[130];
 *     char        datSet[256];
 *     uint32_t    confRev;
 *     int         smpMod;
 *     int         smpMod_present;
 *     uint16_t    smpRate;
 *     uint8_t     optFlds[1];
 *     uint8_t     dstAddr[6];
 *     uint8_t     dstPriority;
 *     uint16_t    dstVid;
 *     uint16_t    dstAppId;
 *     int         dstAddress_present;
 * } cms_msvcb_t;
 */
public class CmsMsvcb extends Structure {
    public CmsBoolean svEna = new CmsBoolean();
    public byte[] msvID = new byte[130];
    public byte[] datSet = new byte[256];
    public CmsInt32U confRev = new CmsInt32U();
    public int smpMod;
    public CmsBoolean smpModPresent = new CmsBoolean();
    public CmsInt16U smpRate = new CmsInt16U();
    public byte[] optFlds = new byte[1];
    public byte[] dstAddr = new byte[6];
    public byte dstPriority;
    public CmsInt16U dstVid = new CmsInt16U();
    public CmsInt16U dstAppId = new CmsInt16U();
    public CmsBoolean dstAddressPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("svEna", "msvID", "datSet", "confRev",
                "smpMod", "smpModPresent", "smpRate", "optFlds",
                "dstAddr", "dstPriority", "dstVid", "dstAppId", "dstAddressPresent");
    }
}
