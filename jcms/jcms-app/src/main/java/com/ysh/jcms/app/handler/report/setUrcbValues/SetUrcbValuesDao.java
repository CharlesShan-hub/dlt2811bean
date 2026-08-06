package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.report.CmsSetUrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesRequest;
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
    private String datSet;
    private Integer bufTm;
    private Integer intgPd;
    private Boolean gi;
    private Boolean resv;

    @Override
    public CmsType toRequest() {
        CmsSetUrcbValuesRequest req = new CmsSetUrcbValuesRequest();
        CmsSetUrcbEntry entry = new CmsSetUrcbEntry().reference(ref != null ? ref : "");

        if (rptId != null)
            entry.rptID(rptId);
        if (rptEna != null)
            entry.rptEna(rptEna);
        if (datSet != null)
            entry.datSet(datSet);
        if (bufTm != null)
            entry.bufTm(bufTm);
        if (intgPd != null)
            entry.intgPd(intgPd);
        if (gi != null)
            entry.gi(gi);
        if (resv != null)
            entry.resv(resv);

        req.urcb.add(entry);
        return req;
    }
}
