package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclSGCBState;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetEditSGValue;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.Map;

public class SetEditSGValueHandler extends AbstractCmsServiceHandler<CmsSetEditSGValue> {

    public SetEditSGValueHandler() {
        super(ServiceName.SET_EDIT_SG_VALUE, CmsSetEditSGValue::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        if (asdu.data == null || asdu.data.size() == 0) {
            return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        String ref = asdu.data.get(0).reference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String sgcbRef = extractSgcbRef(ref);
        if (sgcbRef == null || !resolveSgcbRef(sgcbRef)) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(serverSession);
        SclSGCBState state = sgcbStates.get(sgcbRef);
        if (state == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (!state.isCnfEdit()) {
            log.warn("[Server] SetEditSGValue: cnfEdit is false for {}, edit not confirmed", sgcbRef);
        }

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new);
        boolean hasAnyError = false;

        for (int i = 0; i < asdu.data.size(); i++) {
            String itemRef = asdu.data.get(i).reference.get();
            if (itemRef == null || itemRef.isEmpty()) {
                results.add(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
                hasAnyError = true;
            } else {
                state.getEditValues().put(itemRef, asdu.data.get(i).value);
                results.add(new CmsServiceError(CmsServiceError.NO_ERROR));
            }
        }

        if (hasAnyError) {
            CmsSetEditSGValue response = new CmsSetEditSGValue(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            return new CmsApdu(response);
        }

        return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private String extractSgcbRef(String dataRef) {
        int slashIdx = dataRef.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = dataRef.substring(0, slashIdx);
        String rest = dataRef.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return null;
        String lnName = rest.substring(0, dotIdx);
        return ldName + "/" + lnName + ".SGCB";
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

    @Override
    protected CmsApdu buildNegativeResponse(int errorCode) {
        return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.getReqId())
                .addResult(errorCode));
    }
}
