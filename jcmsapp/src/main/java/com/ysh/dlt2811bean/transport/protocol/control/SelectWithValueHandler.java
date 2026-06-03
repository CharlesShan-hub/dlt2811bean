package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsSelectWithValue;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SelectWithValueHandler extends AbstractCmsServiceHandler<CmsSelectWithValue> {

    public SelectWithValueHandler() {
        super(ServiceName.SELECT_WITH_VALUE, CmsSelectWithValue::new);
    }

    @Override
    protected CmsApdu doServerHandle() {
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] SelectWithValue: empty reference");
            return buildNegativeResponse(CmsAddCause.NOT_SUPPORTED);
        }

        CmsSelectWithValue response = new CmsSelectWithValue(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] SelectWithValue: {}", ref);
        return new CmsApdu(response);
    }

    @Override
    protected CmsApdu buildNegativeResponse(int errorCode) {
        CmsSelectWithValue response = new CmsSelectWithValue(MessageType.RESPONSE_NEGATIVE)
                .reqId(asdu.reqId().get())
                .reference(asdu.reference.get())
                .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(false))
                .addCause(errorCode);
        return new CmsApdu(response);
    }
}
