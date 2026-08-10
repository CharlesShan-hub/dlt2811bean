package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryError;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryRequest;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.service.SclDatasetService;
import com.ysh.jcms.utils.scl.service.SclDatasetService.DataSetResolution;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetDataSetDirectoryServer extends BaseServerHandler<CmsGetDataSetDirectoryRequest, CmsGetDataSetDirectoryError> {

    public GetDataSetDirectoryServer() {
        super(ServiceName.GET_DATA_SET_DIRECTORY, CmsGetDataSetDirectoryRequest.class, CmsGetDataSetDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetDataSetDirectoryRequest req, int reqId) {
        log.info("GetDataSetDirectory from {}: reqId={}", session.sessionId(), reqId);

        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        DataSetResolution dsr = SclDatasetService.resolveDataSet(ied, ap, ref);
        if (dsr == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclDataSet dataSet = dsr.dataSet;
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        CmsGetDataSetDirectoryResponse resp = new CmsGetDataSetDirectoryResponse();
        int ps = pageSize(), count = 0;

        for (SclFCDA fcda : dataSet.fcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) {
                    refAfter = null;
                }
                continue;
            }
            resp.memberData.add(new CmsDataRefFcEntry().reference(fcda.buildFcdaRef())
                    .fc(fcda.fc() != null ? CmsFC.fromCodeOr(fcda.fc(), CmsFC.XX) : 0));
            if (++count >= ps)
                break;
        }
        resp.moreFollows(count >= ps);
        log.info("GetDataSetDirectory: '{}' -> {} members", ref, count);
        return ok(resp, reqId);
    }
}
