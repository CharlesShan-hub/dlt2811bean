package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.bitarray.CmsMsvcbOptFlds;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.msv.CmsSetMsvcbEntry;
import com.ysh.jcms.core.pdu.msv.CmsSetMsvcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetMsvcbValuesDao extends BaseDao {
    private String ref;
    private Boolean svEna;
    private String msvId;
    private String datSet;
    private Integer smpMod;
    private Integer smpRate;
    private Integer optFlds;

    @Override
    public CmsType toRequest() {
        CmsSetMsvcbEntry entry = new CmsSetMsvcbEntry().reference(ref != null ? ref : "");
        if (svEna != null) {
            entry.svEna(svEna);
        }
        if (msvId != null && !msvId.isEmpty()) {
            entry.msvID(msvId);
        }
        if (datSet != null && !datSet.isEmpty()) {
            entry.datSet(datSet);
        }
        if (smpMod != null) {
            entry.smpMod(smpMod);
        }
        if (smpRate != null) {
            entry.smpRate(smpRate);
        }
        if (optFlds != null) {
            CmsMsvcbOptFlds f = new CmsMsvcbOptFlds();
            f.value(optFlds);
            entry.optFlds(f);
        }
        CmsSetMsvcbValuesRequest req = new CmsSetMsvcbValuesRequest();
        req.msvcb.add(entry);
        return req;
    }
}
