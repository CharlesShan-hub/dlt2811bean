package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.data.sequence.report.CmsSetBrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetBrcbValuesRequest;

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

    public SetBrcbValuesDao ref(String v) {
        this.ref = v;
        return this;
    }
    public SetBrcbValuesDao rptId(String v) {
        this.rptId = v;
        return this;
    }
    public SetBrcbValuesDao rptEna(Boolean v) {
        this.rptEna = v;
        return this;
    }
    public SetBrcbValuesDao datSet(String v) {
        this.datSet = v;
        return this;
    }
    public SetBrcbValuesDao bufTm(Integer v) {
        this.bufTm = v;
        return this;
    }
    public SetBrcbValuesDao intgPd(Integer v) {
        this.intgPd = v;
        return this;
    }
    public SetBrcbValuesDao gi(Boolean v) {
        this.gi = v;
        return this;
    }
    public SetBrcbValuesDao purgeBuf(Boolean v) {
        this.purgeBuf = v;
        return this;
    }
    public SetBrcbValuesDao resvTms(Integer v) {
        this.resvTms = v;
        return this;
    }

    public String ref() {
        return ref;
    }

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
