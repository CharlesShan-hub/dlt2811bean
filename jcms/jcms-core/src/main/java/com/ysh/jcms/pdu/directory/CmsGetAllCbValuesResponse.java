package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.choice.CmsCbValueChoice;
import com.ysh.jcms.data.core.CmsField;
import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.InnerAnonymousGetAllCBValuesResponsePDUCbValue;
import com.ysh.jcms.data.InnerGetAllCBValuesResponsePDU;
import com.ysh.jcms.data.InnerSubReference;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.sequence.directory.CmsCbValueEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * GetAllCBValues-ResponsePDU ::= SEQUENCE { reqId Int16U, cbValue [0] IMPLICIT
 * SEQUENCE OF CBValueEntry, moreFollows [1] IMPLICIT BOOLEAN DEFAULT TRUE } —
 * 8.3.6
 */
public class CmsGetAllCbValuesResponse extends CmsSequence {

    public List<CmsCbValueEntry> cbValue; /* SEQUENCE OF CBValueEntry */

    @CmsField
    public CmsBoolean moreFollows; /* DEFAULT TRUE */

    public CmsGetAllCbValuesResponse() {
        super(new InnerGetAllCBValuesResponsePDU());
        this.cbValue = new ArrayList<>();
        this.moreFollows.value(true);
    }

    public CmsGetAllCbValuesResponse cbValue(List<CmsCbValueEntry> v) {
        this.cbValue = v;
        return this;
    }
    public CmsGetAllCbValuesResponse moreFollows(boolean v) {
        this.moreFollows.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        InnerGetAllCBValuesResponsePDU inner = (InnerGetAllCBValuesResponsePDU) this.inner;
        inner.cbValue.value.clear();
        for (CmsCbValueEntry entry : cbValue) {
            InnerAnonymousGetAllCBValuesResponsePDUCbValue innerEntry = new InnerAnonymousGetAllCBValuesResponsePDUCbValue();
            entry.reference.syncToInner();
            innerEntry.reference = (InnerSubReference) entry.reference.inner;
            int ch = entry.value.choice();
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
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        InnerGetAllCBValuesResponsePDU inner = (InnerGetAllCBValuesResponsePDU) this.inner;
        cbValue = new ArrayList<>();
        for (InnerAnonymousGetAllCBValuesResponsePDUCbValue innerEntry : inner.cbValue.value) {
            CmsCbValueEntry entry = new CmsCbValueEntry();
            entry.reference.inner = innerEntry.reference;
            entry.reference.syncFromInner();
            String ch = innerEntry.value._choice;
            if ("brcb".equals(ch)) { entry.value.choice(CmsCbValueChoice.BRCB); entry.value.altBrcb.inner = innerEntry.value.brcb; ((CmsSequence) entry.value.altBrcb).rebindWrappers(); }
            else if ("urcb".equals(ch)) { entry.value.choice(CmsCbValueChoice.URCB); entry.value.altUrcb.inner = innerEntry.value.urcb; ((CmsSequence) entry.value.altUrcb).rebindWrappers(); }
            else if ("lcb".equals(ch)) { entry.value.choice(CmsCbValueChoice.LCB); entry.value.altLcb.inner = innerEntry.value.lcb; ((CmsSequence) entry.value.altLcb).rebindWrappers(); }
            else if ("sgcb".equals(ch)) { entry.value.choice(CmsCbValueChoice.SGECB); entry.value.altSgecb.inner = innerEntry.value.sgcb; ((CmsSequence) entry.value.altSgecb).rebindWrappers(); }
            else if ("gocb".equals(ch)) { entry.value.choice(CmsCbValueChoice.GOCB); entry.value.altGocb.inner = innerEntry.value.gocb; ((CmsSequence) entry.value.altGocb).rebindWrappers(); }
            else if ("msvcb".equals(ch)) { entry.value.choice(CmsCbValueChoice.MSVCB); entry.value.altMsvcb.inner = innerEntry.value.msvcb; ((CmsSequence) entry.value.altMsvcb).rebindWrappers(); }
            cbValue.add(entry);
        }
    }
}
