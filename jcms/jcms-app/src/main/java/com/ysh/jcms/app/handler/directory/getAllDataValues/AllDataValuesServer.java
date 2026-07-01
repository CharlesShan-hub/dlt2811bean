package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.directory.CmsDataValueEntry;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.util.SclDataConverter;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class AllDataValuesServer extends BaseServerHandler {

    public AllDataValuesServer() {
        super(ServiceName.GET_ALL_DATA_VALUES, CmsGetAllDataValuesRequest.class, CmsGetAllDataValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetAllDataValuesRequest req = (CmsGetAllDataValuesRequest) rawReq;
        int reqId = req.reqId.value();
        String refAfter = opt(req.refAfterPresent, req.refAfter);
        log.info("GetAllDataValues from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        String ldName = null, lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME)
            ldName = str(req.reference.altLdName);
        else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE)
            lnReference = str(req.reference.altLnReference);

        List<SclLN> lns = server.resolveLns(ldName, lnReference);
        if (lns == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String fcFilter = null;
        if (req.fcPresent.value()) {
            int fcVal = req.fc.value();
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                fcFilter = FunctionalConstraint.values()[fcVal].name();
                if ("XX".equals(fcFilter)) fcFilter = null;
            }
        }

        List<CmsDataValueEntry> entries = new ArrayList<>();
        int ps = pageSize();
        for (SclLN ln : lns) {
            for (String name : ln.getDataObjectNames(templates)) {
                if (refAfter != null) {
                    if (name.equals(refAfter)) refAfter = null;
                    continue;
                }
                String fullRef = (ldName != null ? ldName + "/" + ln.getFullName() : lnReference) + "." + name;
                SclDataValue sv = server.resolveDataValue(fullRef, templates, fcFilter);
                if (sv != null && sv.val != null && !sv.val.isEmpty() && sv.bType != null && !sv.bType.isEmpty()) {
                    entries.add(new CmsDataValueEntry().reference(name).value(SclDataConverter.toCmsData(sv)));
                }
                if (entries.size() >= ps) break;
            }
            if (entries.size() >= ps) break;
        }

        CmsGetAllDataValuesResponse resp = new CmsGetAllDataValuesResponse().reqId(reqId);
        for (CmsDataValueEntry e : entries) resp.data.add(e);
        resp.moreFollows(entries.size() >= ps);
        log.info("GetAllDataValues: returning {} entries", entries.size());
        return ok(resp, reqId);
    }
}
