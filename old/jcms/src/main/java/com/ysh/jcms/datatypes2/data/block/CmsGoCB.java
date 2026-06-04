package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;

import java.util.Arrays;
import java.util.List;

/**
 * cms_gocb_t — GoCB (GOOSE Control Block).
 *
 * C: typedef struct {
 *     int         goEna;
 *     char        goID[130];
 *     char        datSet[256];
 *     uint32_t    confRev;
 *     int         ndsCom;
 *     uint8_t     dstAddr[6];
 *     uint8_t     dstPriority;
 *     uint16_t    dstVid;
 *     uint16_t    dstAppId;
 *     int         dstAddress_present;
 * } cms_gocb_t;
 */
public class CmsGoCB extends Structure {
    public CmsBoolean goEna = new CmsBoolean();
    public byte[] goID = new byte[130];
    public byte[] datSet = new byte[256];
    public CmsInt32U confRev = new CmsInt32U();
    public CmsBoolean ndsCom = new CmsBoolean();
    public byte[] dstAddr = new byte[6];
    public byte dstPriority;
    public CmsInt16U dstVid = new CmsInt16U();
    public CmsInt16U dstAppId = new CmsInt16U();
    public CmsBoolean dstAddressPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("goEna", "goID", "datSet", "confRev", "ndsCom",
                "dstAddr", "dstPriority", "dstVid", "dstAppId", "dstAddressPresent");
    }
}
