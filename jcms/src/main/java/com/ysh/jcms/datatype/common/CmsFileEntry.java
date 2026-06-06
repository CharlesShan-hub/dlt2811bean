package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.datatype.extended.CmsUtcTime;
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
public class CmsFileEntry extends CmsType {
    public CmsUint8Array.ByValue fileName = new CmsUint8Array.ByValue(129);
    public CmsInt32U.ByValue fileSize = new CmsInt32U.ByValue();
    public CmsUtcTime.ByValue lastModified = new CmsUtcTime.ByValue();
    public CmsInt32U.ByValue checkSum = new CmsInt32U.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("fileName", "fileSize", "lastModified", "checkSum");
    }

    public static class ByValue extends CmsFileEntry implements Structure.ByValue {}
}