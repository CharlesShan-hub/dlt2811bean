package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.sequence.report.CmsSetBrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class SetBrcbValuesDao extends BaseDao {
    private String ref;
    private String rptId;
    private Boolean rptEna;
    private String datSet;
    private Integer bufTm;
    private Integer intgPd;
    private Boolean gi;
    private Boolean purgeBuf;
    private Integer resvTms;

    CmsSetBrcbValuesRequest toRequest(int reqId) {
        CmsSetBrcbValuesRequest req = new CmsSetBrcbValuesRequest();
        CmsSetBrcbEntry entry = new CmsSetBrcbEntry().reference(ref != null ? ref : "");

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
        if (purgeBuf != null)
            entry.purgeBuf(purgeBuf);
        if (resvTms != null)
            entry.resvTms(resvTms);

        req.brcb.add(entry);
        return req;
    }
}
