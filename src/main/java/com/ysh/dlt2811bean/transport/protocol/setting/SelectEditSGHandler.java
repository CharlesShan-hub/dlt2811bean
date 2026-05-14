package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectEditSG;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SelectEditSGHandler extends AbstractCmsServiceHandler<CmsSelectEditSG> {

    public SelectEditSGHandler() {
        super(ServiceName.SELECT_EDIT_SG, CmsSelectEditSG::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String ref = asdu.sgcbReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int sgNum = asdu.settingGroupNumber.get() & 0xFF;
        log.debug("[Server] SelectEditSG: ref={}, sgNum={}", ref, sgNum);

        CmsSelectEditSG response = new CmsSelectEditSG(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        return new CmsApdu(response);
    }
}
