package com.ysh.dlt2811bean.service.svc.rpc.datatypes;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;
import com.ysh.dlt2811bean.datatypes.type.CmsType;

public class CmsReqDataChoice extends AbstractCmsChoice<CmsReqDataChoice> {

    public CmsData reqData = new CmsData<>();
    public CmsOctetString callID = new CmsOctetString().max(255);

    public CmsReqDataChoice() {
        super("ReqDataChoice", 0);
        registerAlternative("reqData");
        registerAlternative("callID");
    }

    public CmsReqDataChoice selectReqData() { select(0); return this; }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CmsReqDataChoice reqData(CmsType<?> val) {
        this.reqData = new CmsData(val);
        return this;
    }

    public CmsReqDataChoice selectCallID() { select(1); return this; }

    public CmsReqDataChoice callID(byte[] id) {
        this.callID.set(id);
        return this;
    }

    @Override
    public CmsReqDataChoice copy() {
        CmsReqDataChoice clone = new CmsReqDataChoice();
        clone.selectedIndex = this.selectedIndex;
        clone.reqData = this.reqData.copy();
        clone.callID = this.callID.copy();
        return clone;
    }
}
