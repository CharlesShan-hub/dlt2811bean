package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.data.CmsSubRefEntry;
import com.ysh.jcms.core.pdu.data.CmsGetDataDirectoryError;
import com.ysh.jcms.core.pdu.data.CmsGetDataDirectoryRequest;
import com.ysh.jcms.core.pdu.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.scl.service.SclDataDirectoryService;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class GetDataDirectoryServer extends BaseServerHandler<CmsGetDataDirectoryRequest, CmsGetDataDirectoryError> {

    public GetDataDirectoryServer() {
        super(CmsServiceInfo.GET_DATA_DIRECTORY, CmsGetDataDirectoryRequest.class, CmsGetDataDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetDataDirectoryRequest req, int reqId) {
        log.info("GetDataDirectory from {}: reqId={}", session.sessionId(), reqId);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        String ref = str(req.dataReference);
        log.info("GetDataDirectory ref='{}'", ref);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        // Parse reference
        SclRef parsed = SclRefParser.parse(ref);
        Navigator nav = Navigator.go(ied, parsed);
        if (!nav.isValid())
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclLN ln = nav.ln();
        SclDOI doi = nav.doi();
        String doName = parsed.doName();
        String sdoName = parsed.daName();

        // Get all entries from service
        List<CmsSubRefEntry> allEntries = SclDataDirectoryService.getDataDirectory(doc, ln, doName, sdoName, doi);
        if (allEntries == null) {
            log.debug("GetDataDirectory: '{}' is not an SDO, returning empty", sdoName);
            allEntries = new ArrayList<>();
        }

        // referenceAfter pagination
        int startIdx = afterIndex(allEntries, refAfter);
        if (startIdx < 0)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Build paged response
        CmsGetDataDirectoryResponse resp = new CmsGetDataDirectoryResponse();
        int ps = pageSize();
        int count = 0;
        for (int i = startIdx; i < allEntries.size() && count < ps; i++) {
            resp.dataAttribute.add(allEntries.get(i));
            count++;
        }
        resp.moreFollows(allEntries.size() > startIdx + ps);
        log.info("GetDataDirectory: '{}' -> {} entries (pageSize={})", ref, count, ps);
        return ok(resp, reqId);
    }

    /** Compute starting index for referenceAfter pagination. */
    private static int afterIndex(List<CmsSubRefEntry> entries, String after) {
        if (after == null || after.isEmpty())
            return 0;
        for (int i = 0; i < entries.size(); i++) {
            String ref = entries.get(i).reference.value();
            if (after.equals(ref))
                return i + 1;
        }
        return -1;
    }
}
