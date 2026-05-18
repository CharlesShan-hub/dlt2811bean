package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.*;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsConfirmEditSGValues;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

import java.util.Map;

public class ConfirmEditSGValuesHandler extends AbstractCmsServiceHandler<CmsConfirmEditSGValues> {

    public ConfirmEditSGValuesHandler() {
        super(ServiceName.CONFIRM_EDIT_SG_VALUES, CmsConfirmEditSGValues::new);
       }

    @Override
    protected CmsApdu doServerHandle() {

        String ref = asdu.sgcbReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        log.debug("[Server] ConfirmEditSGValues: ref={}", ref);

        if (!resolveSgcbRef(ref)) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(serverSession);
        SclSGCBState state = sgcbStates.computeIfAbsent(ref, k -> new SclSGCBState());

        state.setCnfEdit(true);
        log.debug("[Server] ConfirmEditSGValues: confirmed edit for {}", ref);

        CmsConfirmEditSGValues response = new CmsConfirmEditSGValues(MessageType.RESPONSE_POSITIVE)
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
