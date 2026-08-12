package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.report.CmsSetUrcbEntry;
import com.ysh.jcms.core.pdu.report.CmsSetUrcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetUrcbValuesDao extends BaseDao {
    private String ref;
    private String rptId;
    private Boolean rptEna;
    private Boolean resv;
    private String datSet;
    private Integer optFlds;
    private Integer bufTm;
    private Integer trgOps;
    private Integer intgPd;
    private Boolean gi;

    @Override
    public CmsType toRequest() {
        CmsSetUrcbValuesRequest req = new CmsSetUrcbValuesRequest();
        CmsSetUrcbEntry entry = new CmsSetUrcbEntry().reference(ref != null ? ref : "");

        if (rptId != null)
            entry.rptID(rptId);
        if (rptEna != null)
            entry.rptEna(rptEna);
        if (resv != null)
            entry.resv(resv);
        if (datSet != null)
            entry.datSet(datSet);
        if (optFlds != null) {
            CmsRcbOptFlds f = new CmsRcbOptFlds();
            f.value(optFlds);
            entry.optFlds(f);
        }
        if (bufTm != null)
            entry.bufTm(bufTm);
        if (trgOps != null) {
            CmsTriggerConditions t = new CmsTriggerConditions();
            t.value(trgOps);
            entry.trgOps(t);
        }
        if (intgPd != null)
            entry.intgPd(intgPd);
        if (gi != null)
            entry.gi(gi);

        req.urcb.add(entry);
        return req;
    }
}
