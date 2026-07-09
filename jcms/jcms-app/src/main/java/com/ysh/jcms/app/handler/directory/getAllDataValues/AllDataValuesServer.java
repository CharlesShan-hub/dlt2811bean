package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.directory.CmsDataValueEntry;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.convert.DataConverter;
import com.ysh.jcms.utils.scl2.convert.DataValueResolver;
import com.ysh.jcms.utils.scl2.convert.DataValueEntry;
import com.ysh.jcms.utils.scl2.model.ied.SclLN;
import com.ysh.jcms.utils.scl2.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl2.model.ied.SclServer;
import com.ysh.jcms.utils.scl2.model.ied.SclIED;
import com.ysh.jcms.utils.scl2.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl2.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl2.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl2.model.template.SclDO;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class AllDataValuesServer extends BaseServerHandler {

    public AllDataValuesServer() {
        super(ServiceName.GET_ALL_DATA_VALUES, CmsGetAllDataValuesRequest.class, CmsGetAllDataValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetAllDataValuesRequest req = (CmsGetAllDataValuesRequest) rawReq;
        int reqId = req.reqId.value();
        String refAfter = opt(req.refAfterPresent, req.refAfter);
        log.info("GetAllDataValues from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = getScl2Document(session);
        if (doc == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String ldName = null, lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME)
            ldName = str(req.reference.altLdName);
        else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE)
            lnReference = str(req.reference.altLnReference);

        List<SclLN> lns = resolveLns(doc, ldName, lnReference);
        if (lns == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String fcFilter = null;
        if (req.fcPresent.value()) {
            int fcVal = req.fc.value();
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                fcFilter = FunctionalConstraint.values()[fcVal].name();
                if ("XX".equals(fcFilter)) fcFilter = null;
            }
        }

        SclDataTypeTemplates templates = doc.dataTypeTemplates();

        List<CmsDataValueEntry> entries = new ArrayList<>();
        int ps = pageSize();
        outer:
        for (SclLN ln : lns) {
            // Find the IED and LD that contain this LN to build IED-prefixed refs
            String iedName = findIedNameForLn(doc, ln);
            String ldInst = findLdInstForLn(doc, ln);
            if (iedName == null || ldInst == null) continue;

            List<String> doNames = getDoNames(ln, templates);
            for (String doName : doNames) {
                if (refAfter != null) {
                    if (doName.equals(refAfter)) refAfter = null;
                    continue;
                }
                String fullRef = iedName + "/" + ldInst + "/" + ln.getFullName() + "." + doName;
                DataValueEntry dv = DataValueResolver.resolve(doc, fullRef, fcFilter);
                if (dv != null && dv.val() != null && !dv.val().isEmpty() && dv.bType() != null && !dv.bType().isEmpty()) {
                    entries.add(new CmsDataValueEntry().reference(doName).value(DataConverter.toCmsData(dv)));
                }
                if (entries.size() >= ps) break outer;
            }
        }

        CmsGetAllDataValuesResponse resp = new CmsGetAllDataValuesResponse().reqId(reqId);
        for (CmsDataValueEntry e : entries) resp.data.add(e);
        resp.moreFollows(entries.size() >= ps);
        log.info("GetAllDataValues: returning {} entries", entries.size());
        return ok(resp, reqId);
    }

    private static List<SclLN> resolveLns(SclDocument doc, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
            for (SclIED ied : doc.ieds()) {
                for (SclAccessPoint ap : ied.accessPoints()) {
                    SclServer srv = ap.server();
                    if (srv != null) {
                        SclLDevice device = srv.findLDeviceByInst(ldName);
                        if (device != null) {
                            result.addAll(device.lns());
                            return result;
                        }
                    }
                }
            }
            return null;
        }
        if (lnReference == null || lnReference.isEmpty()) return null;
        int slashIdx = lnReference.indexOf('/');
        if (slashIdx < 0) return null;
        String refLd = lnReference.substring(0, slashIdx);
        String refLn = lnReference.substring(slashIdx + 1);
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    SclLDevice device = srv.findLDeviceByInst(refLd);
                    if (device != null) {
                        SclLN ln = device.findLnByFullName(refLn);
                        if (ln != null) {
                            result.add(ln);
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String findIedNameForLn(SclDocument doc, SclLN ln) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    for (SclLDevice ld : srv.lDevices()) {
                        if (ld.findLnByFullName(ln.getFullName()) != null) {
                            return ied.name();
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String findLdInstForLn(SclDocument doc, SclLN ln) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    for (SclLDevice ld : srv.lDevices()) {
                        if (ld.findLnByFullName(ln.getFullName()) != null) {
                            return ld.inst();
                        }
                    }
                }
            }
        }
        return null;
    }

    private static List<String> getDoNames(SclLN ln, SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty()) return names;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
        if (lnt == null) return names;
        for (SclDO doDef : lnt.dos()) {
            names.add(doDef.name());
        }
        return names;
    }
}
