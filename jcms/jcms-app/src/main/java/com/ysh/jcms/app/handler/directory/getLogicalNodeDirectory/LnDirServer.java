package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.svc.directory.CmsAcsiClass;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LnDirServer extends BaseServerHandler {

    /** Max entries per page to avoid exceeding APDU size. */
    private static final int MAX_PAGE_SIZE = 500;

    public LnDirServer() {
        super(ServiceName.GET_LOGIC_NODE_DIRECTORY, CmsGetLogicalNodeDirectoryRequest.class, CmsGetLogicalNodeDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetLogicalNodeDirectoryRequest req = (CmsGetLogicalNodeDirectoryRequest) rawReq;
        int reqId = req.reqId.value();
        int acsiClass = req.acsiClass.value();
        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        log.info("GetLogicalNodeDirectory from {}: reqId={}, acsiClass={}",
            session.getSessionId(), reqId, acsiClass);

        SclServer server = getSclServer(session);
        if (server == null) {
            return buildNodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.len > 0
                ? new String(req.reference.altLdName.value(), StandardCharsets.UTF_8) : null;
        } else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = req.reference.altLnReference.len > 0
                ? new String(req.reference.altLnReference.value(), StandardCharsets.UTF_8) : null;
        }

        List<SclLN> lns = server.resolveLns(ldName, lnReference);
        if (lns == null) {
            return buildNodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<String> names = collectNamesByAcsiClass(lns, acsiClass, templates, refAfter);
        if (names == null) {
            return buildNodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsGetLogicalNodeDirectoryResponse resp = new CmsGetLogicalNodeDirectoryResponse()
            .reqId(reqId);

        boolean more = names.size() > MAX_PAGE_SIZE;
        int limit = more ? MAX_PAGE_SIZE : names.size();
        for (int i = 0; i < limit; i++) {
            resp.reference.add(new CmsSubReference(names.get(i)));
        }
        resp.moreFollows(more);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetLogicalNodeDirectoryResponse", e);
            return buildNodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private List<String> collectNamesByAcsiClass(List<SclLN> lns, int acsiClass,
                                                  SclDataTypeTemplates templates,
                                                  String after) {
        List<String> all = new ArrayList<>();
        for (SclLN ln : lns) {
            switch (acsiClass) {
                case CmsAcsiClass.DATA_OBJECT:
                    if (templates != null) {
                        all.addAll(ln.getDataObjectNames(templates));
                    } else {
                        for (SclDOI doi : ln.getDois()) {
                            all.add(doi.getName());
                        }
                    }
                    break;
                case CmsAcsiClass.DATA_SET:
                    for (SclDataSet ds : ln.getDataSets()) {
                        all.add(ds.getName());
                    }
                    break;
                case CmsAcsiClass.BRCB:
                    for (SclReportControl rc : ln.getReportControls()) {
                        if (rc.isBuffered()) all.add(rc.getName());
                    }
                    break;
                case CmsAcsiClass.URCB:
                    for (SclReportControl rc : ln.getReportControls()) {
                        if (!rc.isBuffered()) all.add(rc.getName());
                    }
                    break;
                case CmsAcsiClass.LCB:
                    for (SclLogControl lc : ln.getLogControls()) {
                        all.add(lc.getName());
                    }
                    break;
                case CmsAcsiClass.GOCB:
                    for (SclGSEControl gc : ln.getGseControls()) {
                        all.add(gc.getName());
                    }
                    break;
                case CmsAcsiClass.MSVCB:
                    for (SclSampledValueControl sv : ln.getSvControls()) {
                        all.add(sv.getName());
                    }
                    break;
                default:
                    break;
            }
        }

        if (all.isEmpty()) return all;

        if (after != null && !after.isEmpty()) {
            int idx = all.indexOf(after);
            if (idx < 0) return null;
            return all.subList(idx + 1, all.size());
        }
        return all;
    }

    private static int pageSize() {
        return CmsConfigLoader.load().getProtocol().getDirectory().getMaxPageSize();
    }

    private Frame buildNodeError(int reqId, int errorCode) {
        return buildError(new CmsGetLogicalNodeDirectoryError()
            .reqId(reqId)
            .serviceError(errorCode)
            .encode(), reqId);
    }
}
