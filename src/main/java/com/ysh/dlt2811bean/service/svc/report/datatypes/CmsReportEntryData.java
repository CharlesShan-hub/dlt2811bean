package com.ysh.dlt2811bean.service.svc.report.datatypes;

import com.ysh.dlt2811bean.datatypes.code.CmsReasonCode;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.string.CmsFC;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsReportEntryData extends AbstractCmsCompound<CmsReportEntryData> {

    public CmsObjectReference reference = new CmsObjectReference();
    public CmsFC fc = new CmsFC();
    public CmsInt16U id = new CmsInt16U();
    public CmsData<?> value = new CmsData<>();
    public CmsReasonCode reason = new CmsReasonCode();

    public CmsReportEntryData() {
        super("ReportEntryData");
        registerOptionalField("reference");
        registerOptionalField("fc");
        registerField("id");
        registerField("value");
        registerOptionalField("reason");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsReportEntryData value(CmsType<?> val) {
        this.value = new CmsData(val);
        return this;
    }

    @Override
    public CmsReportEntryData copy() {
        CmsReportEntryData copy = new CmsReportEntryData();
        copy.reference = reference.copy();
        copy.fc = fc.copy();
        copy.id = id.copy();
        copy.value = value.copy();
        copy.reason = reason.copy();
        return copy;
    }
}
