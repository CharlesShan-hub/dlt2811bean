package com.ysh.dlt2811bean.service.svc.setting.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsBinaryTime;
import com.ysh.dlt2811bean.datatypes.string.CmsEntryID;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsLogStatusValue extends AbstractCmsCompound<CmsLogStatusValue> {

    public CmsBinaryTime oldEntrTm = new CmsBinaryTime();
    public CmsBinaryTime newEntrTm = new CmsBinaryTime();
    public CmsEntryID oldEntr = new CmsEntryID();
    public CmsEntryID newEntr = new CmsEntryID();

    public CmsLogStatusValue() {
        super("LogStatusValue");
        registerField("oldEntrTm");
        registerField("newEntrTm");
        registerField("oldEntr");
        registerField("newEntr");
    }

    @Override
    public CmsLogStatusValue copy() {
        CmsLogStatusValue copy = new CmsLogStatusValue();
        copy.oldEntrTm = oldEntrTm.copy();
        copy.newEntrTm = newEntrTm.copy();
        copy.oldEntr = oldEntr.copy();
        copy.newEntr = newEntr.copy();
        return copy;
    }
}
