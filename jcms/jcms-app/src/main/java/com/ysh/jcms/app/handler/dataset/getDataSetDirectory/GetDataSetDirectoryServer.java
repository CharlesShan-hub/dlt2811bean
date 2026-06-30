package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryRequest;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
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

public class GetDataSetDirectoryServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataSetDirectoryServer.class);

    public GetDataSetDirectoryServer() {
        super(ServiceName.GET_DATA_SET_DIRECTORY, CmsGetDataSetDirectoryRequest.class, CmsGetDataSetDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataSetDirectoryRequest req = (CmsGetDataSetDirectoryRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetDataSetDirectory from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ref = req.datasetReference.len > 0
            ? new String(req.datasetReference.value(), StandardCharsets.UTF_8) : null;
        if (ref == null || ref.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

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
        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsGetDataSetDirectoryResponse resp = new CmsGetDataSetDirectoryResponse()
            .reqId(reqId);
        resp.memberData.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        int pageSize = pageSize();
        int count = 0;

        boolean skipUntilAfter = (refAfter != null && !refAfter.isEmpty());
        for (SclFCDA fcda : dataSet.getFcDas()) {
            if (skipUntilAfter) {
                String fcdaRef = fcda.buildFcdaRef();
                if (fcdaRef.equals(refAfter)) {
                    skipUntilAfter = false;
                }
                continue;
            }

            String fcdaRef = fcda.buildFcdaRef();
            int fcVal = 0;
            if (fcda.getFc() != null && !fcda.getFc().isEmpty()) {
                fcVal = com.ysh.jcms.data.fc.CmsFC.fromCode(fcda.getFc());
            }

            resp.memberData.add(new CmsDataRefFcEntry()
                .reference(fcdaRef)
                .fc(fcVal));
            count++;
            if (count >= pageSize) break;
        }

        resp.moreFollows(count >= pageSize);

        log.info("GetDataSetDirectory: '{}' -> {} members", ref, count);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetDataSetDirectoryResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
