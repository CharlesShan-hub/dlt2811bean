package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectActiveSG;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

import java.util.Map;

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

        if (!resolveSgcbRef(ref)) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(serverSession);
        SclSGCBState state = sgcbStates.get(ref);
        if (state == null) {
            state = new SclSGCBState();
            sgcbStates.put(ref, state);
        }

        if (sgNum < 1 || sgNum > state.getNumOfSG()) {
            log.warn("[Server] SelectActiveSG: invalid sgNum {} for ref {} (numOfSG={})", sgNum, ref, state.getNumOfSG());
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        state.setActSG(sgNum);
        state.setActTm(System.currentTimeMillis() / 1000);
        log.debug("[Server] SelectActiveSG: activated SG {} for {}", sgNum, ref);

        CmsSelectActiveSG response = new CmsSelectActiveSG(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        return new CmsApdu(response);
    }

    private boolean resolveSgcbRef(String ref) {
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return false;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return false;
        String lnName = rest.substring(0, dotIdx);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return false;
        SclLN ln = device.findLnByFullName(lnName);
        return ln != null;
    }
}
