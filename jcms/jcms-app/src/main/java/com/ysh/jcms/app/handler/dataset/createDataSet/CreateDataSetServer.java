package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetError;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetRequest;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.svc.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

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
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ref = req.datasetReference.len > 0
            ? new String(req.datasetReference.value(), StandardCharsets.UTF_8) : null;
        if (ref == null || ref.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String lnName = rest.substring(0, dotIdx);
        String dsName = rest.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Check if refAfter present — if so, extend existing dataset
        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        SclDataSet dataSet;
        if (refAfter != null && !refAfter.isEmpty()) {
            // Extend existing dataset: find it first
            dataSet = ln.findDataSetByName(dsName);
            if (dataSet == null) {
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
        } else {
            // Create new dataset
            boolean persistent = com.ysh.jcms.utils.config.CmsConfigLoader.load()
                .getProtocol().getDataset().isSetDataSetPersistent();
            dataSet = new SclDataSet();
            dataSet.setName(dsName);
            dataSet.setDynamic(!persistent);
            ln.addDataSet(dataSet);
        }

        for (int i = 0; i < req.memberData.count; i++) {
            CmsDataRefFcEntry src = req.memberData.items.get(i);
            String memberRef = src.reference.len > 0
                ? new String(src.reference.value(), StandardCharsets.UTF_8) : null;
            if (memberRef == null || memberRef.isEmpty()) continue;

            SclFCDA fcda = server.parseRefToFcda(memberRef);
            if (fcda == null) {
                log.warn("CreateDataSet: cannot resolve member ref={}", memberRef);
                continue;
            }
            int fcVal = src.fc.value();
            String fcCode = null;
            if (fcVal >= 0 && fcVal < com.ysh.jcms.info.FunctionalConstraint.values().length) {
                fcCode = com.ysh.jcms.info.FunctionalConstraint.values()[fcVal].name();
            }
            if (fcCode != null && !"XX".equals(fcCode)) {
                fcda.setFc(fcCode);
            }
            dataSet.addFcda(fcda);
        }

        log.info("CreateDataSet: '{}' -> {} members (dynamic={})", ref, dataSet.getFcDas().size(), dataSet.isDynamic());

        try {
            return buildSuccess(new CmsCreateDataSetResponse().reqId(reqId).encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode CreateDataSetResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
