package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.svc.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetBrcbEntry;

public class SetBrcbValuesDao {
    private String ref;

    public SetBrcbValuesDao ref(String v) { this.ref = v; return this; }
    public String ref() { return ref; }

    CmsSetBrcbValuesRequest toRequest(int reqId) {
        CmsSetBrcbValuesRequest req = new CmsSetBrcbValuesRequest().reqId(reqId);
        CmsSetBrcbEntry entry = new CmsSetBrcbEntry()
            .reference(ref != null ? ref : "");
        req.brcb.add(entry);
        return req;
    }
}
