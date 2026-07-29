package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.pdu.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryError;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryRequest;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryResponse;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDataSetDirectoryServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataSetDirectoryServer.class);

    public GetDataSetDirectoryServer() {
        super(ServiceName.GET_DATA_SET_DIRECTORY, CmsGetDataSetDirectoryRequest.class, CmsGetDataSetDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetDataSetDirectoryRequest req = (CmsGetDataSetDirectoryRequest) rawReq;
        log.info("GetDataSetDirectory from {}: reqId={}", session.getSessionId(), reqId);

        SclIED ied = requireIed(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName"
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String dsName = ref.substring(dotIdx + 1);

        SclLDevice device = findLd(ied, ldName);
        if (device == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);

        CmsGetDataSetDirectoryResponse resp = new CmsGetDataSetDirectoryResponse().reqId(reqId);
        int ps = pageSize(), count = 0;

        for (SclFCDA fcda : dataSet.fcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) {
                    refAfter = null;
                }
                continue;
            }
            resp.memberData
                    .add(new CmsDataRefFcEntry().reference(fcda.buildFcdaRef()).fc(fcda.fc() != null ? CmsFC.fromCode(fcda.fc()) : 0));
            if (++count >= ps)
                break;
        }
        resp.moreFollows(count >= ps);
        log.info("GetDataSetDirectory: '{}' -> {} members", ref, count);
        return ok(resp, reqId);
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }
}
