package com.ysh.jcms.app.handler.log.setLcbValues;

import com.ysh.jcms.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.sequence.log.CmsSetLcbEntry;
import com.ysh.jcms.pdu.log.CmsSetLcbValuesRequest;

public class SetLcbValuesDao {
    private String ref;
    private Boolean logEna;
    private String datSet;
    private Integer trgOps;
    private Integer intgPd;
    private String logRef;
    private Integer optFlds;
    private Integer bufTm;

    public SetLcbValuesDao ref(String v) {
        this.ref = v;
        return this;
    }
    public SetLcbValuesDao logEna(Boolean v) {
        this.logEna = v;
        return this;
    }
    public SetLcbValuesDao datSet(String v) {
        this.datSet = v;
        return this;
    }
    public SetLcbValuesDao trgOps(Integer v) {
        this.trgOps = v;
        return this;
    }
    public SetLcbValuesDao intgPd(Integer v) {
        this.intgPd = v;
        return this;
    }
    public SetLcbValuesDao logRef(String v) {
        this.logRef = v;
        return this;
    }
    public SetLcbValuesDao optFlds(Integer v) {
        this.optFlds = v;
        return this;
    }
    public SetLcbValuesDao bufTm(Integer v) {
        this.bufTm = v;
        return this;
    }

    public String ref() {
        return ref;
    }

    CmsSetLcbValuesRequest toRequest(int reqId) {
        CmsSetLcbValuesRequest req = new CmsSetLcbValuesRequest();
        CmsSetLcbEntry entry = new CmsSetLcbEntry().reference(ref != null ? ref : "");

        if (logEna != null) {
            entry.logEna(logEna);
        }
        if (datSet != null)
            entry.datSet(datSet);
        if (trgOps != null) {
            entry.trgOps(new CmsTriggerConditions().integrity((trgOps & 1) != 0).data_change((trgOps & 2) != 0)
                    .quality_change((trgOps & 4) != 0).data_update((trgOps & 8) != 0).general_interrogation((trgOps & 16) != 0));
        }
        if (intgPd != null) {
            entry.intgPd(intgPd);
        }
        if (logRef != null)
            entry.logRef(logRef);
        if (optFlds != null) {
            entry.optFlds(new CmsLcbOptFlds().bit0((optFlds & 1) != 0));
        }
        if (bufTm != null) {
            entry.bufTm(bufTm);
        }

        req.lcb.add(entry);
        return req;
    }
}
