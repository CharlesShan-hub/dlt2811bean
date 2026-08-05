package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetError;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetRequest;
import com.ysh.jcms.pdu.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class CreateDataSetServer extends BaseServerHandler {


    public CreateDataSetServer() {
        super(ServiceName.CREATE_DATA_SET, CmsCreateDataSetRequest.class, CmsCreateDataSetError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsCreateDataSetRequest req = (CmsCreateDataSetRequest) rawReq;
        log.info("CreateDataSet from {}: reqId={}, {} members", session.getSessionId(), reqId, req.memberData.size());

        SclIED ied = requireIed(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName" — the dsName is the last dot-segment
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String ldName = ref.substring(0, slashIdx);
        String lnNamePart = ref.substring(slashIdx + 1, dotIdx);
        String dsName = ref.substring(dotIdx + 1);

        // Resolve LN first (DataSet may not exist yet since we're creating it)
        SclLDevice device = findLd(ied, ldName);
        if (device == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnNamePart);
        if (ln == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;
        boolean isPersistent = CmsConfigLoader.load().getProtocol().getDataset().isSetDataSetPersistent();

        SclDataSet dataSet;
        if (refAfter != null) {
            dataSet = ln.findDataSetByName(dsName);
            if (dataSet == null)
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        } else {
            dataSet = new SclDataSet();
            dataSet.name(dsName);
            dataSet.dynamic(!isPersistent);
            ln.addDataSet(dataSet);
        }

        int added = 0, failed = 0;
        for (CmsDataRefFcEntry src : req.memberData) {
            String memberRef = str(src.reference);
            if (memberRef == null) {
                failed++;
                continue;
            }

            SclFCDA fcda = parseRefToFcda(ied, memberRef);
            if (fcda == null) {
                log.warn("CreateDataSet: cannot resolve {}", memberRef);
                failed++;
                continue;
            }

            String fcCode = fcCode(src.fc.value());
            if (fcCode != null)
                fcda.fc(fcCode);
            dataSet.addFcda(fcda);
            added++;
        }
        if (added == 0)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        log.info("CreateDataSet: '{}' -> {} members ({} failed, dynamic={})", ref, dataSet.fcDas().size(), failed, dataSet.dynamic());
        return ok(new CmsCreateDataSetResponse(), reqId);
    }

    private static SclFCDA parseRefToFcda(SclIED ied, String ref) {
        if (ref == null || ref.isEmpty())
            return null;

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0)
            return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0)
            return null;
        String lnPart = rest.substring(0, dotIdx);
        String doDaPart = rest.substring(dotIdx + 1);

        SclLDevice device = findLd(ied, ldName);
        if (device == null)
            return null;

        SclLN ln = device.findLnByFullName(lnPart);
        if (ln == null)
            return null;

        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(ldName);
        fcda.lnClass(ln.lnClass());
        fcda.lnInst(ln.inst());
        fcda.prefix(ln.prefix() != null ? ln.prefix() : "");

        int daDotIdx = doDaPart.indexOf('.');
        if (daDotIdx >= 0) {
            fcda.doName(doDaPart.substring(0, daDotIdx));
            fcda.daName(doDaPart.substring(daDotIdx + 1));
        } else {
            fcda.doName(doDaPart);
        }

        return fcda;
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }
}
