package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsTimeActivatedOperateTermination;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class TimeActivatedOperateTerminationHandler extends AbstractCmsServiceHandler<CmsTimeActivatedOperateTermination> {

    public TimeActivatedOperateTerminationHandler() {
        super(ServiceName.TIME_ACTIVATED_OPERATE_TERMINATION, CmsTimeActivatedOperateTermination::new);
    }

    @Override
    protected CmsApdu doServerHandle() {
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] TimeActivatedOperateTermination: empty reference");
            return buildNegativeResponse(CmsAddCause.NOT_SUPPORTED);
        }

        CmsTimeActivatedOperateTermination response = new CmsTimeActivatedOperateTermination(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());

        log.debug("[Client] TimeActivatedOperateTermination acknowledged: {}", ref);
        return new CmsApdu(response);
    }
}
