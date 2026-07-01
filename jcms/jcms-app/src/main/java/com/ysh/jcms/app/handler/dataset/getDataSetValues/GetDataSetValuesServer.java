package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesRequest;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.util.SclDataConverter;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDataSetValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataSetValuesServer.class);

    public GetDataSetValuesServer() {
        super(ServiceName.GET_DATA_SET_VALUES, CmsGetDataSetValuesRequest.class, CmsGetDataSetValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataSetValuesRequest req = (CmsGetDataSetValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetDataSetValues from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        String ref = str(req.datasetReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName"
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String dsName = ref.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);

        CmsGetDataSetValuesResponse resp = new CmsGetDataSetValuesResponse().reqId(reqId);
        resp.value.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        int ps = pageSize(), count = 0;

        for (SclFCDA fcda : dataSet.getFcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) { refAfter = null; }
                continue;
            }
            SclDataValue sv = server.resolveDataValue(fcda.buildFcdaRef(), templates, fcda.getFc());
            if (sv != null && sv.val != null && !sv.val.isEmpty()) {
                resp.value.add(SclDataConverter.toCmsData(sv));
                if (++count >= ps) break;
            }
        }
        resp.moreFollows(count >= ps);
        log.info("GetDataSetValues: '{}' -> {} values", ref, count);
        return ok(resp, reqId);
    }
}
