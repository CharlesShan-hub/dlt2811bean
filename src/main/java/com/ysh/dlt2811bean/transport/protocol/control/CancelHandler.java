package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsCancel;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class CancelHandler extends AbstractCmsServiceHandler<CmsCancel> {

    public CancelHandler() {
        super(ServiceName.CANCEL, CmsCancel::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsCancel asdu = (CmsCancel) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] Cancel: empty reference");
            return buildNegativeResponse(request, CmsAddCause.NOT_SUPPORTED);
        }

        CmsCancel response = new CmsCancel(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] Cancel: {}", ref);
        return new CmsApdu(response);
    }

    @Override
    protected CmsApdu buildNegativeResponse(CmsApdu request, int errorCode) {
        CmsCancel asdu = (CmsCancel) request.getAsdu();
        CmsCancel response = new CmsCancel(MessageType.RESPONSE_NEGATIVE)
                .reqId(asdu.reqId().get())
                .reference(asdu.reference.get())
                .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(false))
                .addCause(errorCode);
        return new CmsApdu(response);
    }
}
