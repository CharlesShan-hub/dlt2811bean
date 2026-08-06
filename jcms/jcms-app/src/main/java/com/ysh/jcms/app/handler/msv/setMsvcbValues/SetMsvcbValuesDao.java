package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.msv.CmsSetMsvcbEntry;
import com.ysh.jcms.pdu.msv.CmsSetMsvcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetMsvcbValuesDao extends BaseDao {
    private String ref;
    private String svEna;
    private String msvId;
    private String datSet;

    @Override
    public CmsType toRequest() {
        CmsSetMsvcbEntry entry = new CmsSetMsvcbEntry().reference(ref != null ? ref : "");
        if (svEna != null && !svEna.isEmpty())
            entry.svEna(Boolean.parseBoolean(svEna));
        if (msvId != null && !msvId.isEmpty())
            entry.msvID(msvId);
        if (datSet != null && !datSet.isEmpty())
            entry.datSet(datSet);
        CmsSetMsvcbValuesRequest req = new CmsSetMsvcbValuesRequest();
        req.msvcb.add(entry);
        return req;
    }
}
