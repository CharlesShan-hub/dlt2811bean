package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsStructure;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclSGCBState;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetEditSGValue;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.Map;

public class GetEditSGValueHandler extends AbstractCmsServiceHandler<CmsGetEditSGValue> {

    public GetEditSGValueHandler() {
        super(ServiceName.GET_EDIT_SG_VALUE, CmsGetEditSGValue::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        log.debug("[Server] GetEditSGValue: {} entries", asdu.data.size());

        if (asdu.data == null || asdu.data.size() == 0) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String ref = asdu.data.get(0).reference.get();
        log.debug("[Server] GetEditSGValue: ref={}", ref);
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String sgcbRef = extractSgcbRef(ref);
        log.debug("[Server] GetEditSGValue: sgcbRef={}", sgcbRef);
        if (sgcbRef == null || !resolveSgcbRef(sgcbRef)) {
            log.warn("[Server] GetEditSGValue: resolveSgcbRef failed for sgcbRef={}", sgcbRef);
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(serverSession);
        SclSGCBState state = sgcbStates.get(sgcbRef);
        log.debug("[Server] GetEditSGValue: state found={}, editValues={}", state != null, state != null ? state.getEditValues().size() : 0);
        if (state == null) {
            log.warn("[Server] GetEditSGValue: no state found for sgcbRef={}, available keys={}", sgcbRef, sgcbStates.keySet());
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsGetEditSGValue response = new CmsGetEditSGValue(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.value = new CmsStructure();
        for (Map.Entry<String, CmsData<?>> entry : state.getEditValues().entrySet()) {
            response.value.add(entry.getValue().get());
        }
        response.moreFollows.set(false);

        return new CmsApdu(response);
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
        return new CmsApdu(new CmsGetEditSGValue(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.getReqId())
                .serviceError(errorCode));
    }
}
