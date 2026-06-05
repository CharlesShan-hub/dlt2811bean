package com.ysh.jcms.datatype.block;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt16U;
import com.ysh.jcms.datatype.basic.CmsInt8U;
import com.ysh.jcms.datatype.common.CmsTimeStamp;
import com.ysh.jcms.ffi.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsSgcb extends CmsType {
    public CmsInt8U.ByValue numOfSG = new CmsInt8U.ByValue();
    public CmsInt8U.ByValue actSG = new CmsInt8U.ByValue();
    public CmsInt8U.ByValue editSG = new CmsInt8U.ByValue();
    public CmsTimeStamp.ByValue tActEdt = new CmsTimeStamp.ByValue();
    public CmsInt16U.ByValue resvTms = new CmsInt16U.ByValue();
    public CmsBoolean.ByValue resvTms_present = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("numOfSG", "actSG", "editSG", "tActEdt",
                "resvTms", "resvTms_present");
    }

    @Override
    protected int encodeBufSize() { return 256; }

    public static class ByValue extends CmsSgcb implements Structure.ByValue {}
}