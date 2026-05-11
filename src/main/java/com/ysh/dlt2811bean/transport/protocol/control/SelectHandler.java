package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsSelect;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SelectHandler extends AbstractCmsServiceHandler<CmsSelect> {

    public SelectHandler() {
        super(ServiceName.SELECT, CmsSelect::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsSelect asdu = (CmsSelect) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] Select: empty reference");
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsSelect response = new CmsSelect(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref);

        log.debug("[Server] Select: {}", ref);
        return new CmsApdu(response);
    }
}
