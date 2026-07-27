package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAnonymousGetAllCBValuesResponsePDUCbValue;
import com.ysh.jcms.data.InnerGetAllCBValuesResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import java.util.ArrayList;
import java.util.List;

/**
 * GetAllCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U, cbValue [0] IMPLICIT
 * SEQUENCE OF CBValueEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.3.6
 */
public class CmsGetAllCbValuesResponse extends CmsType {

    public List<CmsCbValueEntry> cbValue; /* SEQUENCE OF CBValueEntry */

    public boolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllCbValuesResponse() {
        super(new InnerGetAllCBValuesResponsePDU());
        this.cbValue = new ArrayList<>();
    }

    public CmsGetAllCbValuesResponse cbValue(List<CmsCbValueEntry> v) {
        this.cbValue = v;
        return this;
    }
    public CmsGetAllCbValuesResponse moreFollows(boolean v) {
        this.moreFollows = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetAllCBValuesResponsePDU inner = (InnerGetAllCBValuesResponsePDU) this.inner;
        inner.cbValue.value.clear();
        for (CmsCbValueEntry entry : cbValue) {
            InnerAnonymousGetAllCBValuesResponsePDUCbValue innerEntry = new InnerAnonymousGetAllCBValuesResponsePDUCbValue();
            innerEntry.reference = (InnerSubReference) entry.reference.inner;
            int ch = entry.value.choice.value();
            switch (ch) {
                case CmsCbValueChoice.BRCB: innerEntry.value._choice = "brcb"; entry.value.altBrcb.syncToInner(); innerEntry.value.brcb = (com.ysh.jcms.data.InnerBRCB) entry.value.altBrcb.inner; break;
                case CmsCbValueChoice.URCB: innerEntry.value._choice = "urcb"; entry.value.altUrcb.syncToInner(); innerEntry.value.urcb = (com.ysh.jcms.data.InnerURCB) entry.value.altUrcb.inner; break;
                case CmsCbValueChoice.LCB: innerEntry.value._choice = "lcb"; entry.value.altLcb.syncToInner(); innerEntry.value.lcb = (com.ysh.jcms.data.InnerLCB) entry.value.altLcb.inner; break;
                case CmsCbValueChoice.SGECB: innerEntry.value._choice = "sgcb"; entry.value.altSgecb.syncToInner(); innerEntry.value.sgcb = (com.ysh.jcms.data.InnerSGCB) entry.value.altSgecb.inner; break;
                case CmsCbValueChoice.GOCB: innerEntry.value._choice = "gocb"; entry.value.altGocb.syncToInner(); innerEntry.value.gocb = (com.ysh.jcms.data.InnerGoCB) entry.value.altGocb.inner; break;
                case CmsCbValueChoice.MSVCB: innerEntry.value._choice = "msvcb"; entry.value.altMsvcb.syncToInner(); innerEntry.value.msvcb = (com.ysh.jcms.data.InnerMSVCB) entry.value.altMsvcb.inner; break;
            }
            inner.cbValue.value.add(innerEntry);
        }
        inner.moreFollows.value = moreFollows ? 1 : 0;
    }

    @Override
    public void syncFromInner() {
        InnerGetAllCBValuesResponsePDU inner = (InnerGetAllCBValuesResponsePDU) this.inner;
        cbValue = new ArrayList<>();
        for (InnerAnonymousGetAllCBValuesResponsePDUCbValue innerEntry : inner.cbValue.value) {
            CmsCbValueEntry entry = new CmsCbValueEntry();
            entry.reference.inner = innerEntry.reference;
            String ch = innerEntry.value._choice;
            if ("brcb".equals(ch)) { entry.value.choice.value(CmsCbValueChoice.BRCB); entry.value.altBrcb.inner = innerEntry.value.brcb; entry.value.altBrcb.syncFromInner(); }
            else if ("urcb".equals(ch)) { entry.value.choice.value(CmsCbValueChoice.URCB); entry.value.altUrcb.inner = innerEntry.value.urcb; entry.value.altUrcb.syncFromInner(); }
            else if ("lcb".equals(ch)) { entry.value.choice.value(CmsCbValueChoice.LCB); entry.value.altLcb.inner = innerEntry.value.lcb; entry.value.altLcb.syncFromInner(); }
            else if ("sgcb".equals(ch)) { entry.value.choice.value(CmsCbValueChoice.SGECB); entry.value.altSgecb.inner = innerEntry.value.sgcb; entry.value.altSgecb.syncFromInner(); }
            else if ("gocb".equals(ch)) { entry.value.choice.value(CmsCbValueChoice.GOCB); entry.value.altGocb.inner = innerEntry.value.gocb; entry.value.altGocb.syncFromInner(); }
            else if ("msvcb".equals(ch)) { entry.value.choice.value(CmsCbValueChoice.MSVCB); entry.value.altMsvcb.inner = innerEntry.value.msvcb; entry.value.altMsvcb.syncFromInner(); }
            cbValue.add(entry);
        }
        this.moreFollows = inner.moreFollows.value() != 0;
    }
}
