package com.ysh.jcms.app.handler.report.setUrcbValues;

import com.ysh.jcms.data.sequence.report.CmsSetUrcbEntry;
import com.ysh.jcms.pdu.report.CmsSetUrcbValuesRequest;

public class SetUrcbValuesDao {
    private String ref;
    private String rptId;
    private Boolean rptEna;
    private String datSet;
    private Integer bufTm;
    private Integer intgPd;
    private Boolean gi;
    private Boolean resv;

    public SetUrcbValuesDao ref(String v) {
        this.ref = v;
        return this;
    }
    public SetUrcbValuesDao rptId(String v) {
        this.rptId = v;
        return this;
    }
    public SetUrcbValuesDao rptEna(Boolean v) {
        this.rptEna = v;
        return this;
    }
    public SetUrcbValuesDao datSet(String v) {
        this.datSet = v;
        return this;
    }
    public SetUrcbValuesDao bufTm(Integer v) {
        this.bufTm = v;
        return this;
    }
    public SetUrcbValuesDao intgPd(Integer v) {
        this.intgPd = v;
        return this;
    }
    public SetUrcbValuesDao gi(Boolean v) {
        this.gi = v;
        return this;
    }
    public SetUrcbValuesDao resv(Boolean v) {
        this.resv = v;
        return this;
    }

    public String ref() {
        return ref;
    }

    CmsSetUrcbValuesRequest toRequest() {
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
