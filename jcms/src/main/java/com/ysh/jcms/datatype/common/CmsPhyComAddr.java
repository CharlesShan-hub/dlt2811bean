package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsInt16U;
import com.ysh.jcms.datatype.basic.CmsInt8U;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
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
public class CmsPhyComAddr extends CmsType {
    public CmsUint8Array.ByValue addr = new CmsUint8Array.ByValue();
    public CmsInt8U.ByValue priority = new CmsInt8U.ByValue();
    public CmsInt16U.ByValue vid = new CmsInt16U.ByValue();
    public CmsInt16U.ByValue appid = new CmsInt16U.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("addr", "priority", "vid", "appid");
    }

    public static class ByValue extends CmsPhyComAddr implements Structure.ByValue {}
}