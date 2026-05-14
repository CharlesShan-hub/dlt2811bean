package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsTimeActivatedOperate;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class TimeActivatedOperateHandler extends AbstractCmsServiceHandler<CmsTimeActivatedOperate> {

    public TimeActivatedOperateHandler() {
        super(ServiceName.TIME_ACTIVATED_OPERATE, CmsTimeActivatedOperate::new);
    }

    @Override
    protected CmsApdu doServerHandle() {
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] TimeActivatedOperate: empty reference");
            return buildNegativeResponse(CmsAddCause.NOT_SUPPORTED);
        }

        CmsTimeActivatedOperate response = new CmsTimeActivatedOperate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] TimeActivatedOperate: {}", ref);
        return new CmsApdu(response);
    }

    @Override
    protected CmsApdu buildNegativeResponse(int errorCode) {
        CmsTimeActivatedOperate response = new CmsTimeActivatedOperate(MessageType.RESPONSE_NEGATIVE)
                .reqId(asdu.reqId().get())
                .reference(asdu.reference.get())
                .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(false))
                .addCause(errorCode);
        return new CmsApdu(response);
    }
}
