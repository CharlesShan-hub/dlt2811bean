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
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
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

        // Parse "LD0/LLN0.dsName"
        if (!SclRefParser.isValid(ref))
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnNamePart = sclRef.lnName();
        String dsName = sclRef.doName();
        if (dsName == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

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
        if (ref == null || ref.isEmpty() || !SclRefParser.isValid(ref))
            return null;

        SclRef sclRef = SclRefParser.parse(ref);
        SclLDevice device = findLd(ied, sclRef.ldInst());
        if (device == null)
            return null;

        SclLN ln = device.findLnByFullName(sclRef.lnName());
        if (ln == null)
            return null;

        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(sclRef.ldInst());
        fcda.lnClass(ln.lnClass());
        fcda.lnInst(ln.inst());
        fcda.prefix(ln.prefix() != null ? ln.prefix() : "");
        fcda.doName(sclRef.doName());
        fcda.daName(sclRef.daName());

        return fcda;
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }
}
