package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class AllDataValuesServer extends BaseServerHandler {

    public AllDataValuesServer() {
        super(ServiceName.GET_ALL_DATA_VALUES, CmsGetAllDataValuesRequest.class, CmsGetAllDataValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetAllDataValuesRequest req = (CmsGetAllDataValuesRequest) rawReq;
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;
        log.info("GetAllDataValues from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        String ldName = null, lnReference = null;
        if (req.reference.choice() == CmsReferenceChoice.LD_NAME)
            ldName = req.reference.altLdName.value();
        else if (req.reference.choice() == CmsReferenceChoice.LN_REFERENCE)
            lnReference = str(req.reference.altLnReference);

        List<SclLN> lns = resolveLns(ied, ldName, lnReference);
        if (lns == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String fcFilter = fcCode(req.isPresent("fc") ? req.fc.value() : -1);

        SclDataTypeTemplates templates = doc.dataTypeTemplates();

        List<CmsDataValueEntry> entries = new ArrayList<>();
        int ps = pageSize();
        outer : for (SclLN ln : lns) {
            // Find the IED and LD that contain this LN to build IED-prefixed refs
            String iedName = findIedNameForLn(ied, ln);
            String ldInst = findLdInstForLn(ied, ln);
            if (iedName == null || ldInst == null)
                continue;

            List<String> doNames = getDoNames(ln, templates);
            for (String doName : doNames) {
                if (refAfter != null) {
                    if (doName.equals(refAfter))
                        refAfter = null;
                    continue;
                }
                String fullRef = iedName + "/" + ldInst + "/" + ln.getFullName() + "." + doName;
                DataValueEntry dv = DataValueResolver.resolve(doc, fullRef, fcFilter);
                if (dv != null && dv.val() != null && !dv.val().isEmpty() && dv.bType() != null && !dv.bType().isEmpty()) {
                    entries.add(new CmsDataValueEntry().reference(doName).value(DataConverter.toCmsData(dv)));
                }
                if (entries.size() >= ps)
                    break outer;
            }
        }

        CmsGetAllDataValuesResponse resp = new CmsGetAllDataValuesResponse();
        for (CmsDataValueEntry e : entries)
            resp.data.add(e);
        resp.moreFollows(entries.size() >= ps);
        log.info("GetAllDataValues: returning {} entries", entries.size());
        return ok(resp, reqId);
    }

    private static List<SclLN> resolveLns(SclIED ied, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
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
            return null;
        }
        if (lnReference == null || lnReference.isEmpty() || !SclRefParser.isValid(lnReference))
            return null;
        SclRef sclRef = SclRefParser.parse(lnReference);
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer srv = ap.server();
            if (srv != null) {
                SclLDevice device = srv.findLDeviceByInst(sclRef.ldInst());
                if (device != null) {
                    SclLN ln = device.findLnByFullName(sclRef.lnName());
                    if (ln != null) {
                        result.add(ln);
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private static String findIedNameForLn(SclIED ied, SclLN ln) {
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
        return null;
    }

    private static String findLdInstForLn(SclIED ied, SclLN ln) {
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
        return null;
    }

    private static List<String> getDoNames(SclLN ln, SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return names;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
        if (lnt == null)
            return names;
        for (SclDO doDef : lnt.dos()) {
            names.add(doDef.name());
        }
        return names;
    }
}
