package com.ysh.jcms.core.pdu.goose;

import java.nio.charset.StandardCharsets;

import com.ysh.jcms.data.InnerSendGOOSEMessagePDU;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.scalar.CmsBoolean;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.data.scalar.CmsString;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@code
 * SendGOOSEMessage-PDU ::= SEQUENCE {
 *     goID            [0] IMPLICIT VisibleString129,
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     goRef           [2] IMPLICIT ObjectReference OPTIONAL,
 *     t               [3] IMPLICIT TimeStamp,
 *     stNum           [4] IMPLICIT INT32U,
 *     sqNum           [5] IMPLICIT INT32U,
 *     simulation      [6] IMPLICIT BOOLEAN,
 *     confRev         [7] IMPLICIT INT32U,
 *     ndsCom          [8] IMPLICIT BOOLEAN,
 *     data            [9] IMPLICIT SEQUENCE OF Data
 * } — 8.9.1
 * }
 * </pre>
 *
 * <p>
 * Unconfirmed service — no Response or Error PDU.
 */
public class CmsSendGooseMessage extends CmsSequence {

    @CmsField
    public CmsString goID;

    @CmsField(optional = true)
    public CmsObjectReference datSet;

    @CmsField(optional = true)
    public CmsObjectReference goRef;

    @CmsField
    public CmsUtcTime t;

    @CmsField
    public CmsInt32U stNum;

    @CmsField
    public CmsInt32U sqNum;

    @CmsField
    public CmsBoolean simulation;

    @CmsField
    public CmsInt32U confRev;

    @CmsField
    public CmsBoolean ndsCom;

    @CmsField(sequenceOf = true, elementType = CmsData.class)
    public List<CmsData> data; /* SEQUENCE OF Data */

    public CmsSendGooseMessage() {
        super(new InnerSendGOOSEMessagePDU());
        this.data = new ArrayList<>();
    }

    public CmsSendGooseMessage goID(String v) {
        this.goID.value(v);
        return this;
    }
    public CmsSendGooseMessage goID(byte[] v) {
        return goID(new String(v, StandardCharsets.UTF_8));
    }
    public CmsSendGooseMessage datSet(String v) {
        if (v != null) {
            this.datSet.value(v);
            setPresent("datSet", true);
        } else {
            setPresent("datSet", false);
        }
        return this;
    }
    public CmsSendGooseMessage datSet(byte[] v) {
        return datSet(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSendGooseMessage goRef(String v) {
        if (v != null) {
            this.goRef.value(v);
            setPresent("goRef", true);
        } else {
            setPresent("goRef", false);
        }
        return this;
    }
    public CmsSendGooseMessage goRef(byte[] v) {
        return goRef(v != null ? new String(v, StandardCharsets.UTF_8) : null);
    }
    public CmsSendGooseMessage t(CmsUtcTime v) {
        this.t.value(v);
        return this;
    }
    public CmsSendGooseMessage stNum(long v) {
        this.stNum.value(v);
        return this;
    }
    public CmsSendGooseMessage sqNum(long v) {
        this.sqNum.value(v);
        return this;
    }
    public CmsSendGooseMessage simulation(boolean v) {
        this.simulation.value(v);
        return this;
    }
    public CmsSendGooseMessage confRev(long v) {
        this.confRev.value(v);
        return this;
    }
    public CmsSendGooseMessage ndsCom(boolean v) {
        this.ndsCom.value(v);
        return this;
    }
    public CmsSendGooseMessage data(List<CmsData> v) {
        this.data = v;
        return this;
    }
}
