package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.data.choice.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LnDirServer extends BaseServerHandler {

    public LnDirServer() {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY, CmsGetLogicalNodeDirectoryRequest.class, CmsGetLogicalNodeDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        long t0 = System.currentTimeMillis();
        CmsGetLogicalNodeDirectoryRequest req = (CmsGetLogicalNodeDirectoryRequest) rawReq;
        int acsiClass = req.acsiClass.value();
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        log.info("GetLogicalNodeDirectory from {}: reqId={}, acsiClass={}", session.getSessionId(), reqId, acsiClass);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        log.info("TIMING: server+templates resolved in {}ms", System.currentTimeMillis() - t0);

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.value();
        } else if (req.reference.choice() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = str(req.reference.altLnReference);
        }

        List<SclLN> lns = resolveLns(ied, ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        log.info("TIMING: resolved {} LNs in {}ms", lns.size(), System.currentTimeMillis() - t0);

        // 获取有效 LD 名：ldName 或从 lnReference 中提取
        String effectiveLd = ldName != null ? ldName : (lnReference != null ? SclRefParser.parse(lnReference).ldName() : null);

        List<String> names = collectNamesByAcsiClass(lns, effectiveLd, acsiClass, templates, refAfter, t0);
        if (names == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        log.info("TIMING: collected {} names in {}ms", names.size(), System.currentTimeMillis() - t0);

        CmsGetLogicalNodeDirectoryResponse resp = new CmsGetLogicalNodeDirectoryResponse();

        int pageSize = pageSize();
        boolean more = names.size() > pageSize;
        int limit = more ? pageSize : names.size();
        for (int i = 0; i < limit; i++) {
            resp.reference.add(new CmsSubReference(names.get(i)));
        }
        resp.moreFollows(more);

        log.info("TIMING: built response array ({} items) in {}ms", limit, System.currentTimeMillis() - t0);
        return ok(resp, reqId);
    }

    private static List<SclLN> resolveLns(SclIED ied, String ldName, String lnReference) {
        if (ldName != null && !ldName.isEmpty()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    SclLDevice device = srv.findLDeviceByInst(ldName);
                    if (device != null)
                        return device.lns();
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
                        List<SclLN> result = new ArrayList<>();
                        result.add(ln);
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private List<String> collectNamesByAcsiClass(List<SclLN> lns, String ldName, int acsiClass, SclDataTypeTemplates templates,
            String after, long t0) {
        List<String> all = new ArrayList<>();
        int lnIdx = 0;
        for (SclLN ln : lns) {
            long tLn = System.currentTimeMillis();
            switch (acsiClass) {
                case CmsAcsiClass.DATA_OBJECT :
                    if (templates != null) {
                        all.addAll(getDataObjectNames(ldName, ln, templates));
                    } else {
                        for (SclDOI doi : ln.dois()) {
                            all.add(ldName + "/" + ln.getFullName() + "." + doi.name());
                        }
                    }
                    break;
                case CmsAcsiClass.DATA_SET :
                    for (SclDataSet ds : ln.dataSets()) {
                        all.add(ds.name());
                    }
                    break;
                case CmsAcsiClass.BRCB :
                    for (SclReportControl rc : ln.reportControls()) {
                        if ("true".equals(rc.buffered()))
                            all.add(rc.name());
                    }
                    break;
                case CmsAcsiClass.URCB :
                    for (SclReportControl rc : ln.reportControls()) {
                        if (!"true".equals(rc.buffered()))
                            all.add(rc.name());
                    }
                    break;
                case CmsAcsiClass.LCB :
                    for (SclLogControl lc : ln.logControls()) {
                        all.add(lc.name());
                    }
                    break;
                case CmsAcsiClass.LOG :
                    for (SclLogControl lc : ln.logControls()) {
                        String logName = lc.logName();
                        if (logName != null && !logName.isEmpty()) {
                            all.add(logName);
                        }
                    }
                    break;
                case CmsAcsiClass.GOCB :
                    for (SclGSEControl gc : ln.gseControls()) {
                        all.add(gc.name());
                    }
                    break;
                case CmsAcsiClass.MSVCB :
                    for (SclSampledValueControl sv : ln.svControls()) {
                        all.add(sv.name());
                    }
                    break;
                default :
                    break;
            }
            log.info("TIMING: LN[{}] {} done in {}ms (total names so far: {})", lnIdx++, ln.getFullName(), System.currentTimeMillis() - tLn,
                    all.size());
        }

        if (all.isEmpty())
            return all;

        if (after != null && !after.isEmpty()) {
            int idx = all.indexOf(after);
            log.info("TIMING: referenceAfter lookup took {}ms", System.currentTimeMillis() - t0);
            if (idx < 0)
                return null;
            return all.subList(idx + 1, all.size());
        }
        return all;
    }

    private static List<String> getDataObjectNames(String ldName, SclLN ln, SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return names;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
        if (lnt == null)
            return names;
        String lnPrefix = ldName + "/" + ln.getFullName() + ".";
        for (SclDO doDef : lnt.dos()) {
            names.add(lnPrefix + doDef.name());
            // 递归收集 SDO（子数据对象）
            collectSdoNames(lnPrefix + doDef.name(), doDef.type(), templates, names, new HashSet<>());
        }
        return names;
    }

    private static void collectSdoNames(String parentRef, String doTypeId, SclDataTypeTemplates templates, List<String> names,
            HashSet<String> visited) {
        if (doTypeId == null || doTypeId.isEmpty() || !visited.add(doTypeId))
            return;
        SclDOType doType = templates.findDoTypeById(doTypeId);
        if (doType == null)
            return;
        for (SclSDO sdo : doType.sdos()) {
            String ref = parentRef + "." + sdo.name();
            names.add(ref);
            // 递归：SDO 的 type 指向另一个 DOType
            collectSdoNames(ref, sdo.type(), templates, names, visited);
        }
    }
}
