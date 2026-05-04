package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.code.CmsReasonCode;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsLogEntryData extends AbstractCmsCompound<CmsLogEntryData> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsFC fc = new CmsFC();
    public CmsData value = new CmsData<>();
    public CmsReasonCode reason = new CmsReasonCode();

    public CmsLogEntryData() {
        super("LogEntryData");
        registerField("reference");
        registerField("fc");
        registerField("value");
        registerField("reason");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsLogEntryData value(CmsType<?> val) {
        this.value = new CmsData(val);
        return this;
    }

    @Override
    public CmsLogEntryData copy() {
        CmsLogEntryData copy = new CmsLogEntryData();
        copy.reference = reference.copy();
        copy.fc = fc.copy();
        copy.value = value.copy();
        copy.reason = reason.copy();
        return copy;
    }
}
