package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectActiveSG;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SelectActiveSGHandler extends AbstractCmsServiceHandler<CmsSelectActiveSG> {

    public SelectActiveSGHandler() {
        super(ServiceName.SELECT_ACTIVE_SG, CmsSelectActiveSG::new);
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SelectActiveSG: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsSelectActiveSG) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsSelectActiveSG asdu = (CmsSelectActiveSG) request.getAsdu();

        String ref = asdu.sgcbReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int sgNum = asdu.settingGroupNumber.get() & 0xFF;
        log.debug("[Server] SelectActiveSG: ref={}, sgNum={}", ref, sgNum);

        CmsSelectActiveSG response = new CmsSelectActiveSG(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsSelectActiveSG request, int errorCode) {
        CmsSelectActiveSG response = new CmsSelectActiveSG(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
