package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.svc.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetBrcbEntry;

public class SetBrcbValuesDao {
    private String ref;
    private String rptId;
    private Boolean rptEna;
    private String datSet;
    private Integer bufTm;
    private Integer intgPd;
    private Boolean gi;
    private Boolean purgeBuf;
    private Integer resvTms;

    public SetBrcbValuesDao ref(String v) { this.ref = v; return this; }
    public SetBrcbValuesDao rptId(String v) { this.rptId = v; return this; }
    public SetBrcbValuesDao rptEna(Boolean v) { this.rptEna = v; return this; }
    public SetBrcbValuesDao datSet(String v) { this.datSet = v; return this; }
    public SetBrcbValuesDao bufTm(Integer v) { this.bufTm = v; return this; }
    public SetBrcbValuesDao intgPd(Integer v) { this.intgPd = v; return this; }
    public SetBrcbValuesDao gi(Boolean v) { this.gi = v; return this; }
    public SetBrcbValuesDao purgeBuf(Boolean v) { this.purgeBuf = v; return this; }
    public SetBrcbValuesDao resvTms(Integer v) { this.resvTms = v; return this; }

    public String ref() { return ref; }

    CmsSetBrcbValuesRequest toRequest(int reqId) {
        CmsSetBrcbValuesRequest req = new CmsSetBrcbValuesRequest().reqId(reqId);
        CmsSetBrcbEntry entry = new CmsSetBrcbEntry()
            .reference(ref != null ? ref : "");

        if (rptId != null) entry.rptId(rptId);
        if (rptEna != null) { entry.rptEnaPresent(true); entry.rptEna(rptEna); }
        if (datSet != null) entry.datSet(datSet);
        if (bufTm != null) { entry.bufTmPresent(true); entry.bufTm(bufTm); }
        if (intgPd != null) { entry.intgPdPresent(true); entry.intgPd(intgPd); }
        if (gi != null) { entry.giPresent(true); entry.gi(gi); }
        if (purgeBuf != null) { entry.purgeBufPresent(true); entry.purgeBuf(purgeBuf); }
        if (resvTms != null) { entry.resvTmsPresent(true); entry.resvTms(resvTms); }

        req.brcb.add(entry);
        return req;
    }
}
