package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.report.CmsSetBrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;

@Setter
@Getter
@Accessors(fluent = true)
public class SetBrcbValuesDao extends BaseDao {
    private String ref;
    private String rptId;
    private Boolean rptEna;
    private String datSet;
    private Integer optFlds;
    private Integer bufTm;
    private Integer trgOps;
    private Integer intgPd;
    private Boolean gi;
    private Boolean purgeBuf;
    private String entryId;
    private Integer resvTms;

    @Override
    public CmsType toRequest() {
        CmsSetBrcbValuesRequest req = new CmsSetBrcbValuesRequest();
        CmsSetBrcbEntry entry = new CmsSetBrcbEntry().reference(ref != null ? ref : "");

        if (rptId != null)
            entry.rptID(rptId);
        if (rptEna != null)
            entry.rptEna(rptEna);
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
        if (purgeBuf != null)
            entry.purgeBuf(purgeBuf);
        if (entryId != null)
            entry.entryID(entryId.getBytes(StandardCharsets.UTF_8));
        if (resvTms != null)
            entry.resvTms(resvTms);

        req.brcb.add(entry);
        return req;
    }
}
