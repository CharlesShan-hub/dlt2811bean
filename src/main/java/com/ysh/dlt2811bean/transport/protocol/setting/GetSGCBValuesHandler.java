package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.model.ied.SclLDevice;
import com.ysh.dlt2811bean.scl.model.ied.SclLN;
import com.ysh.dlt2811bean.scl.model.control.SclSGCBState;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetSGCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorSgcbChoice;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.util.Map;


public class GetSGCBValuesHandler extends AbstractCmsServiceHandler<CmsGetSGCBValues> {

    public GetSGCBValuesHandler() {
        super(ServiceName.GET_SGCB_VALUES, CmsGetSGCBValues::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

        CmsArray<CmsErrorSgcbChoice> choices = new CmsArray<>(CmsErrorSgcbChoice::new);

        for (int i = 0; i < asdu.sgcbReference.size(); i++) {
            String ref = asdu.sgcbReference.get(i).get();
            CmsErrorSgcbChoice choice = buildSgcbChoice(ref);
            choices.add(choice);
        }

        CmsGetSGCBValues response = new CmsGetSGCBValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.errorSgcb = choices;

        log.debug("[Server] GetSGCBValues: {} references", asdu.sgcbReference.size());
        return new CmsApdu(response);
    }

    private CmsErrorSgcbChoice buildSgcbChoice(String ref) {
        CmsErrorSgcbChoice choice = new CmsErrorSgcbChoice();

        if (ref == null || ref.isEmpty()) {
            choice.selectError().error.set(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            return choice;
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }
        String lnName = rest.substring(0, dotIdx);
        String sgcbName = rest.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            choice.selectError().error.set(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return choice;
        }

        Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(serverSession);
        SclSGCBState state = sgcbStates.computeIfAbsent(ref, k -> {
            CmsConfig config = CmsConfigLoader.load();
            return new SclSGCBState(config.getSetting().getNumOfSG());
        });

        CmsSGCB sgcb = new CmsSGCB();
        sgcb.sgcbName.set(sgcbName);
        sgcb.sgcbRef.set(ref);
        sgcb.numOfSG.set(state.getNumOfSG());
        sgcb.actSG.set(state.getActSG());
        sgcb.editSG.set(state.getEditSG());
        sgcb.cnfEdit.set(state.isCnfEdit());
        sgcb.lActTm.secondsSinceEpoch.set(state.getActTm());
        sgcb.resvTms.set(state.getResvTms());
        choice.selectSgcb().sgcb = sgcb;
        return choice;
    }
}
