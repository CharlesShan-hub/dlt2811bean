package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.Structure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.extended.CmsUtcTime;

import java.util.Arrays;
import java.util.List;

/**
 * cms_sgcb_t — SGCB (Setting Group Control Block).
 *
 * C: typedef struct {
 *     uint8_t     numOfSG;
 *     uint8_t     actSG;
 *     uint8_t     editSG;
 *     cms_utc_time_t tActEdt;
 *     uint16_t    resvTms;
 *     int         resvTms_present;
 * } cms_sgcb_t;
 */
public class CmsSgcb extends Structure {
    public byte numOfSG;
    public byte actSG;
    public byte editSG;
    /* padding byte */
    public CmsUtcTime tActEdt = new CmsUtcTime();
    public CmsInt16U resvTms = new CmsInt16U();
    public CmsBoolean resvTmsPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("numOfSG", "actSG", "editSG", "tActEdt",
                "resvTms", "resvTmsPresent");
    }
}
