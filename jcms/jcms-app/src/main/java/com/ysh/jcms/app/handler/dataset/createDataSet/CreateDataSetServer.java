package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetError;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetRequest;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.service.SclDatasetService;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class CreateDataSetServer extends BaseServerHandler<CmsCreateDataSetRequest, CmsCreateDataSetError> {

    public CreateDataSetServer() {
        super(ServiceName.CREATE_DATA_SET, CmsCreateDataSetRequest.class, CmsCreateDataSetError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsCreateDataSetRequest req, int reqId) {
        log.info("CreateDataSet from {}: reqId={}, {} members", session.getSessionId(), reqId, req.memberData.size());

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        String dsName = SclDatasetService.extractDsName(ref);
        if (dsName == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclLN ln = SclDatasetService.resolveLn(ied, ap, ref);
        if (ln == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;
        boolean isPersistent = CmsConfigLoader.load().protocol().dataset().setDataSetPersistent();

        SclDataSet dataSet;
        if (refAfter != null) {
            dataSet = ln.findDataSetByName(dsName);
            if (dataSet == null)
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        } else {
            dataSet = new SclDataSet();
            dataSet.name(dsName);
            dataSet.dynamic(!isPersistent);
            ln.addDataSet(dataSet);
        }

        int added = 0, failed = 0;
        for (CmsDataRefFcEntry src : req.memberData) {
            String memberRef = str(src.reference);
            if (memberRef == null) {
                failed++;
                continue;
            }

            SclFCDA fcda = SclDatasetService.parseRefToFcda(ied, ap, memberRef);
            if (fcda == null) {
                log.warn("CreateDataSet: cannot resolve {}", memberRef);
                failed++;
                continue;
            }

            String fcCode = fcCode(src.fc.value());
            if (fcCode != null)
                fcda.fc(fcCode);
            dataSet.addFcda(fcda);
            added++;
        }
        if (added == 0)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        log.info("CreateDataSet: '{}' -> {} members ({} failed, dynamic={})", ref, dataSet.fcDas().size(), failed, dataSet.dynamic());
        return ok(new CmsCreateDataSetResponse(), reqId);
    }
}
