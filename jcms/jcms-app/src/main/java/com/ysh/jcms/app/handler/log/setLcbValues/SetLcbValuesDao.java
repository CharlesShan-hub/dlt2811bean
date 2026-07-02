package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.data.block.CmsLcbOptFlds;
import com.ysh.jcms.data.block.CmsTriggerConditions;
import com.ysh.jcms.svc.log.CmsSetLcbValuesRequest;
import com.ysh.jcms.svc.log.CmsSetLcbEntry;

public class SetLcbValuesDao {
    private String ref;
    private Boolean logEna;
    private String datSet;
    private Integer trgOps;
    private Integer intgPd;
    private String logRef;
    private Integer optFlds;
    private Integer bufTm;

    public SetLcbValuesDao ref(String v) { this.ref = v; return this; }
    public SetLcbValuesDao logEna(Boolean v) { this.logEna = v; return this; }
    public SetLcbValuesDao datSet(String v) { this.datSet = v; return this; }
    public SetLcbValuesDao trgOps(Integer v) { this.trgOps = v; return this; }
    public SetLcbValuesDao intgPd(Integer v) { this.intgPd = v; return this; }
    public SetLcbValuesDao logRef(String v) { this.logRef = v; return this; }
    public SetLcbValuesDao optFlds(Integer v) { this.optFlds = v; return this; }
    public SetLcbValuesDao bufTm(Integer v) { this.bufTm = v; return this; }

    public String ref() { return ref; }

    CmsSetLcbValuesRequest toRequest(int reqId) {
        CmsSetLcbValuesRequest req = new CmsSetLcbValuesRequest().reqId(reqId);
        CmsSetLcbEntry entry = new CmsSetLcbEntry()
            .reference(ref != null ? ref : "");

        if (logEna != null) { entry.logEnaPresent(true); entry.logEna(logEna); }
        if (datSet != null) entry.datSet(datSet);
        if (trgOps != null) { entry.trgOpsPresent(true); entry.trgOps(new CmsTriggerConditions().integrity((trgOps & 1) != 0).data_change((trgOps & 2) != 0).quality_change((trgOps & 4) != 0).data_update((trgOps & 8) != 0).general_interrogation((trgOps & 16) != 0)); }
        if (intgPd != null) { entry.intgPdPresent(true); entry.intgPd(intgPd); }
        if (logRef != null) entry.logRef(logRef);
        if (optFlds != null) { entry.optFldsPresent(true); entry.optFlds(new CmsLcbOptFlds().value((optFlds & 1) != 0)); }
        if (bufTm != null) { entry.bufTmPresent(true); entry.bufTm(bufTm); }

        req.lcb.add(entry);
        return req;
    }
}
