package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetError;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetRequest;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.svc.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.util.RefUtil;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDataSetServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(CreateDataSetServer.class);

    public CreateDataSetServer() {
        super(ServiceName.CREATE_DATA_SET, CmsCreateDataSetRequest.class, CmsCreateDataSetError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsCreateDataSetRequest req = (CmsCreateDataSetRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("CreateDataSet from {}: reqId={}, {} members", session.getSessionId(), reqId, req.memberData.count);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String ref = str(req.datasetReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        RefUtil.RefParts p = RefUtil.parse(ref);
        if (p == null || p.doName == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String dsName = p.doName;

        // Resolve LN first (DataSet may not exist yet since we're creating it)
        RefUtil.DataResolveResult rr = RefUtil.resolveData(server, p.lnRef());
        if (rr == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = rr.ln;

        String refAfter = opt(req.refAfterPresent, req.refAfter);
        boolean isPersistent = CmsConfigLoader.load().getProtocol().getDataset().isSetDataSetPersistent();

        SclDataSet dataSet;
        if (refAfter != null) {
            dataSet = ln.findDataSetByName(dsName);
            if (dataSet == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        } else {
            dataSet = new SclDataSet();
            dataSet.setName(dsName);
            dataSet.setDynamic(!isPersistent);
            ln.addDataSet(dataSet);
        }

        int added = 0, failed = 0;
        for (int i = 0; i < req.memberData.count; i++) {
            CmsDataRefFcEntry src = req.memberData.items.get(i);
            String memberRef = str(src.reference);
            if (memberRef == null) { failed++; continue; }

            SclFCDA fcda = server.parseRefToFcda(memberRef);
            if (fcda == null) { log.warn("CreateDataSet: cannot resolve {}", memberRef); failed++; continue; }

            int fcVal = src.fc.value();
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                String fcCode = FunctionalConstraint.values()[fcVal].name();
                if (!"XX".equals(fcCode)) fcda.setFc(fcCode);
            }
            dataSet.addFcda(fcda);
            added++;
        }
        if (added == 0) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        log.info("CreateDataSet: '{}' -> {} members ({} failed, dynamic={})", ref, dataSet.getFcDas().size(), failed, dataSet.isDynamic());
        return ok(new CmsCreateDataSetResponse().reqId(reqId), reqId);
    }
}
