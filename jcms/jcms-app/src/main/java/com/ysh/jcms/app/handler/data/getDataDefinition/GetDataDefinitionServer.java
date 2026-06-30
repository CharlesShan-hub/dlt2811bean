package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.data.CmsDataDefResultEntry;
import com.ysh.jcms.svc.data.CmsDataRefEntry;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionError;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionRequest;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataDefinitionEntry;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class GetDataDefinitionServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataDefinitionServer.class);

    public GetDataDefinitionServer() {
        super(ServiceName.GET_DATA_DEFINITION, CmsGetDataDefinitionRequest.class, CmsGetDataDefinitionError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetDataDefinitionRequest req = (CmsGetDataDefinitionRequest) decoded;
        req.data.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataDefinitionRequest req = (CmsGetDataDefinitionRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetDataDefinition from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.data.count);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        CmsGetDataDefinitionResponse resp = new CmsGetDataDefinitionResponse()
            .reqId(reqId);
        resp.data.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();

        int pageSize = pageSize();
        for (int i = 0; i < req.data.count && resp.data.count < pageSize; i++) {
            CmsDataRefEntry refEntry = req.data.items.get(i);
            String ref = refEntry.reference.len > 0
                ? new String(refEntry.reference.value(), StandardCharsets.UTF_8) : null;

            if (ref == null || ref.isEmpty()) {
                log.warn("GetDataDefinition: empty reference at index {}", i);
                continue;
            }

            // Resolve fc from request
            String fcCode = null;
            if (refEntry.fcPresent.value()) {
                int fcVal = refEntry.fc.value();
                if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                    fcCode = FunctionalConstraint.values()[fcVal].name();
                    if ("XX".equals(fcCode)) fcCode = null;
                }
            }

            SclDataDefinitionEntry sclEntry = server.resolveDataDefinition(ref, fcCode, templates);
            if (sclEntry != null) {
                CmsDataDefResultEntry result = new CmsDataDefResultEntry()
                    .definition(sclEntry.definition);
                if (sclEntry.cdcType != null && !sclEntry.cdcType.isEmpty()) {
                    result.cdcType(sclEntry.cdcType);
                }
                resp.data.add(result);
            } else {
                log.warn("GetDataDefinition: cannot resolve definition for ref={}", ref);
            }
        }

        resp.moreFollows(false);

        log.info("GetDataDefinition: returning {} definitions", resp.data.count);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetDataDefinitionResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
