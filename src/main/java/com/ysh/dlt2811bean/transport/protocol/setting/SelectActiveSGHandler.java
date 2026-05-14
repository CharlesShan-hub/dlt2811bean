package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectActiveSG;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SelectActiveSGHandler extends AbstractCmsServiceHandler<CmsSelectActiveSG> {

    public SelectActiveSGHandler() {
        super(ServiceName.SELECT_ACTIVE_SG, CmsSelectActiveSG::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        String ref = asdu.sgcbReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int sgNum = asdu.settingGroupNumber.get() & 0xFF;
        log.debug("[Server] SelectActiveSG: ref={}, sgNum={}", ref, sgNum);

        CmsSelectActiveSG response = new CmsSelectActiveSG(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        return new CmsApdu(response);
    }
}
