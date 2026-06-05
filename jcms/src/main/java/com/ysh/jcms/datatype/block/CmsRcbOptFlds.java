package com.ysh.jcms.datatype.block;

import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsRcbOptFlds extends CmsType {
    public static class ByValue extends CmsRcbOptFlds implements com.sun.jna.Structure.ByValue {}

    public CmsBoolean.ByValue sequence_number = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue report_time_stamp = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue reason_for_inclusion = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue data_set_name = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue data_reference = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue buffer_overflow = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue entry_id = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue conf_revision = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue segmentation = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("sequence_number", "report_time_stamp", "reason_for_inclusion",
                "data_set_name", "data_reference", "buffer_overflow",
                "entry_id", "conf_revision", "segmentation");
    }
}