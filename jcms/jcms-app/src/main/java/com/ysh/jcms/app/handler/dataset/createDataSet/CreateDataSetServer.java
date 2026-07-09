package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetError;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetRequest;
import com.ysh.jcms.svc.dataset.CmsCreateDataSetResponse;
import com.ysh.jcms.svc.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.ied.SclLN;
import com.ysh.jcms.utils.scl2.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl2.model.ied.SclServer;
import com.ysh.jcms.utils.scl2.model.ied.SclIED;
import com.ysh.jcms.utils.scl2.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl2.model.input.SclDataSet;
import com.ysh.jcms.utils.scl2.model.input.SclFCDA;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateDataSetServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(CreateDataSetServer.class);

    public CreateDataSetServer() {
        super(ServiceName.CREATE_DATA_SET, CmsCreateDataSetRequest.class, CmsCreateDataSetError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsCreateDataSetRequest req = (CmsCreateDataSetRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("CreateDataSet from {}: reqId={}, {} members", session.getSessionId(), reqId, req.memberData.count);

        SclDocument doc = getScl2Document(session);
        if (doc == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String ref = str(req.datasetReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName" — the dsName is the last dot-segment
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String ldName = ref.substring(0, slashIdx);
        String lnNamePart = ref.substring(slashIdx + 1, dotIdx);
        String dsName = ref.substring(dotIdx + 1);

        // Resolve LN first (DataSet may not exist yet since we're creating it)
        SclLDevice device = findLd(doc, ldName);
        if (device == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnNamePart);
        if (ln == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);
        boolean isPersistent = CmsConfigLoader.load().getProtocol().getDataset().isSetDataSetPersistent();

        SclDataSet dataSet;
        if (refAfter != null) {
            dataSet = ln.findDataSetByName(dsName);
            if (dataSet == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        } else {
            dataSet = new SclDataSet();
            dataSet.name(dsName);
            dataSet.dynamic(!isPersistent);
            ln.addDataSet(dataSet);
        }

        int added = 0, failed = 0;
        for (int i = 0; i < req.memberData.count; i++) {
            CmsDataRefFcEntry src = req.memberData.items.get(i);
            String memberRef = str(src.reference);
            if (memberRef == null) { failed++; continue; }

            SclFCDA fcda = parseRefToFcda(doc, memberRef);
            if (fcda == null) { log.warn("CreateDataSet: cannot resolve {}", memberRef); failed++; continue; }

            int fcVal = src.fc.value();
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                String fcCode = FunctionalConstraint.values()[fcVal].name();
                if (!"XX".equals(fcCode)) fcda.fc(fcCode);
            }
            dataSet.addFcda(fcda);
            added++;
        }
        if (added == 0) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        log.info("CreateDataSet: '{}' -> {} members ({} failed, dynamic={})", ref, dataSet.fcDas().size(), failed, dataSet.dynamic());
        return ok(new CmsCreateDataSetResponse().reqId(reqId), reqId);
    }

    /**
     * 将引用字符串（如 {@code LD0/MMXU1.Volts.mag}）解析为 SclFCDA，
     * 通过匹配 LN 部分与设备中的实际 LN。
     */
    private static SclFCDA parseRefToFcda(SclDocument doc, String ref) {
        if (ref == null || ref.isEmpty()) return null;

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return null;
        String lnPart = rest.substring(0, dotIdx);
        String doDaPart = rest.substring(dotIdx + 1);

        SclLDevice device = findLd(doc, ldName);
        if (device == null) return null;

        SclLN ln = device.findLnByFullName(lnPart);
        if (ln == null) return null;

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

    /** 跨 IED/AccessPoint 查找指定 LD 的 LDevice。 */
    private static SclLDevice findLd(SclDocument doc, String ldName) {
        SclIED ied = doc.findIedByLdInst(ldName);
        if (ied == null) return null;
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer srv = ap.server();
            if (srv != null) {
                SclLDevice ld = srv.findLDeviceByInst(ldName);
                if (ld != null) return ld;
            }
        }
        return null;
    }
}
