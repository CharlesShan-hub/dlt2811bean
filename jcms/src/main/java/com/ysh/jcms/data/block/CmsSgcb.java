package com.ysh.jcms.data.block;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.*;
import com.ysh.jcms.data.scalar.*;
import java.util.Arrays;
import java.util.List;

/**
 * SGCB ::= SEQUENCE { 6 fields }  —  8.5
 *
 * OPTIONAL field (resvTms) uses a CmsBoolean "present" flag before the value.
 */
public class CmsSgcb extends CmsType {

    public CmsInt8U     numOfSG;
    public CmsInt8U     actSG;
    public CmsInt8U     editSG;
    public CmsTimeStamp tActEdt;
    public CmsBoolean   resvTms_present;
    public CmsInt16U    resvTms;        /* OPTIONAL */

    public CmsSgcb() {
        this.numOfSG  = new CmsInt8U();
        this.actSG    = new CmsInt8U();
        this.editSG   = new CmsInt8U();
        this.tActEdt  = new CmsTimeStamp();
        this.resvTms_present = new CmsBoolean();
        this.resvTms  = new CmsInt16U();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(numOfSG, actSG, editSG, tActEdt,
                             resvTms_present, resvTms);
    }
}
