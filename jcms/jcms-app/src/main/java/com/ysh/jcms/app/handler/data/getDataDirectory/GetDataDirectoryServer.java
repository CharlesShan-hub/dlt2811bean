package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryError;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryRequest;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.svc.data.CmsSubRefEntry;
import com.ysh.jcms.utils.scl.model.data.SclDataDirectoryEntry;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetDataDirectoryServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataDirectoryServer.class);

    public GetDataDirectoryServer() {
        super(ServiceName.GET_DATA_DIRECTORY, CmsGetDataDirectoryRequest.class, CmsGetDataDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataDirectoryRequest req = (CmsGetDataDirectoryRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetDataDirectory from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        String ref = req.dataReference.len > 0
            ? new String(req.dataReference.value(), StandardCharsets.UTF_8) : null;
        if (ref == null || ref.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        // Parse reference: "LD0/LLN0" or "LD0/LLN0.Pos"
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        String lnName = parts[0];
        String doName = parts.length > 1 ? parts[1] : null;

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Collect directory entries
        List<SclDataDirectoryEntry> allEntries;
        if (doName == null) {
            // LN level: list DOs
            allEntries = ln.collectDataDirectory(templates);
        } else {
            // DO level: list DAs and SDIs
            SclDOI doi = ln.findDoiByName(doName);
            if (doi == null) {
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            allEntries = doi.collectDataDirectory(templates, ln);
        }

        // referenceAfter pagination
        int skipCount = 0;
        if (refAfter != null && !refAfter.isEmpty()) {
            boolean found = false;
            for (SclDataDirectoryEntry e : allEntries) {
                if (found) break;
                if (e.ref.equals(refAfter)) {
                    found = true;
                }
                skipCount++;
            }
            if (!found && !allEntries.isEmpty()) {
                return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }

        CmsGetDataDirectoryResponse resp = new CmsGetDataDirectoryResponse()
            .reqId(reqId);
        int pageSize = pageSize();
        resp.dataAttribute.allocSize = Math.max(pageSize, 1);
        int count = 0;
        for (int i = skipCount; i < allEntries.size() && count < pageSize; i++) {
            SclDataDirectoryEntry e = allEntries.get(i);
            CmsSubRefEntry entry = new CmsSubRefEntry()
                .reference(e.ref);
            if (e.fc != null && !e.fc.isEmpty()) {
                entry.fc(com.ysh.jcms.data.fc.CmsFC.fromCode(e.fc));
            }
            resp.dataAttribute.add(entry);
            count++;
        }
        resp.moreFollows(allEntries.size() > skipCount + pageSize);

        log.info("GetDataDirectory: '{}' -> {} entries (pageSize={})", ref, count, pageSize);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception ex) {
            log.error("Failed to encode GetDataDirectoryResponse", ex);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
