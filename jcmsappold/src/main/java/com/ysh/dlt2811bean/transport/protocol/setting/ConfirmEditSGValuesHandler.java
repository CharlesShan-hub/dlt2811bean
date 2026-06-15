package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.type.CmsScalar;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.scl.model.control.SclSGCBState;
import com.ysh.dlt2811bean.scl.model.ied.SclLDevice;
import com.ysh.dlt2811bean.scl.model.ied.SclLN;
import com.ysh.dlt2811bean.scl.model.template.SclDataTypeTemplates;
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
        SclSGCBState state = sgcbStates.get(ref);
        if (state == null) {
            return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (state.isCnfEdit()) {
            log.warn("[Server] ConfirmEditSGValues: cnfEdit already true for {}, nothing to confirm", ref);
            CmsConfirmEditSGValues response = new CmsConfirmEditSGValues(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get());
            return new CmsApdu(response);
        }

        SclDataTypeTemplates templates = (sclDocument != null) ? sclDocument.getDataTypeTemplates() : null;

        int savedCount = 0;
        int errorCount = 0;
        for (Map.Entry<String, CmsData<?>> entry : state.getEditValues().entrySet()) {
            String dataRef = entry.getKey();
            CmsData<?> cmsData = entry.getValue();
            String stringValue = extractStringValue(cmsData);
            if (stringValue == null) {
                log.warn("[Server] ConfirmEditSGValues: cannot extract value from {} for ref {}",
                        cmsData.getClass().getSimpleName(), dataRef);
                errorCount++;
                continue;
            }
            int result = server.setDataValue(dataRef, stringValue, templates);
            if (result == CmsServiceError.NO_ERROR) {
                savedCount++;
            } else {
                log.warn("[Server] ConfirmEditSGValues: failed to set {}: error={}", dataRef, result);
                errorCount++;
            }
        }

        state.getEditValues().clear();
        state.setCnfEdit(true);

        log.info("[Server] ConfirmEditSGValues: confirmed {} for {}, saved {} values ({} errors)",
                ref, savedCount, errorCount);

        if (errorCount > 0) {
            return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        CmsConfirmEditSGValues response = new CmsConfirmEditSGValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        return new CmsApdu(response);
    }

    private String extractStringValue(CmsData<?> cmsData) {
        CmsType<?> innerValue = cmsData.getInnerValue();
        if (innerValue == null) return null;
        if (innerValue instanceof CmsScalar) {
            Object val = ((CmsScalar<?, ?>) innerValue).get();
            return val != null ? val.toString() : null;
        }
        return innerValue.toString();
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
