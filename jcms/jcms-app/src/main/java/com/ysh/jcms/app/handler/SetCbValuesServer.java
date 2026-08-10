package com.ysh.jcms.app.handler;

import com.ysh.jcms.data.core.CmsSequence;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Template for the Set*CBValues family of services (BRCB/URCB/LCB).
 *
 * <p>
 * The protocol shape is identical across all three (8.7.3 / 8.7.5 / 8.8.3):
 * <ol>
 * <li>Empty sequence → Response+</li>
 * <li>Process each entry; a per-entry result that carries any optional field is
 * a failure (each optional field is an error code)</li>
 * <li>Any failure → Response- with all results, otherwise Response+</li>
 * </ol>
 * Subclasses provide the type-specific bits: how to get the entries, how to
 * process one entry, and how to build the success/error PDUs.
 */
public abstract class SetCbValuesServer<R extends CmsType, E extends CmsType, Entry extends CmsType, Result extends CmsSequence>
        extends
            BaseServerHandler<R, E> {

    protected SetCbValuesServer(com.ysh.jcms.utils.transport.ServiceName serviceName, Class<R> requestType, Class<E> errorType) {
        super(serviceName, requestType, errorType);
    }

    /** The entries carried by this request PDU. */
    protected abstract List<Entry> entries(R req);

    /** Extract the control-block reference from one entry. */
    protected abstract String entryRef(Entry entry);

    /** Build the success response PDU (no entries). */
    protected abstract CmsType successResp();

    /** Build the error response PDU. */
    protected abstract E errorResp();

    /** Append one per-entry result to the error response PDU. */
    protected abstract void addResult(E errResp, Result result);

    /** Process a single entry; sets optional error fields on failure. */
    protected abstract Result processEntry(SclIED ied, Entry entry, String ref, Session session);

    @Override
    protected Frame onDecodeSuccess(Session session, R req, int reqId) {
        List<Entry> entries = entries(req);
        log.info("{} from {}: reqId={}, {} entries", getServiceName(), session.getSessionId(), reqId, entries.size());

        if (entries.isEmpty())
            return ok(successResp(), reqId);

        SclIED ied = requireIed(session, reqId);

        List<Result> results = new ArrayList<>();
        boolean hasAnyError = false;
        for (Entry entry : entries) {
            Result result = processEntry(ied, entry, entryRef(entry), session);
            results.add(result);
            if (result.hasAnyPresent())
                hasAnyError = true;
        }

        if (hasAnyError) {
            E errResp = errorResp();
            for (Result r : results)
                addResult(errResp, r);
            log.warn("{}: {} entries had errors", getServiceName(), results.stream().filter(CmsSequence::hasAnyPresent).count());
            try {
                return buildError(errResp.encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode error response", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        return ok(successResp(), reqId);
    }

    /**
     * Parse and validate a control-block reference ({@code LD/LN.cbName}).
     *
     * @return parsed ref, or null if the format is invalid or has no CB name
     */
    protected static SclRef parseRef(String ref) {
        if (!SclRefParser.isValid(ref))
            return null;
        SclRef sclRef = SclRefParser.parse(ref);
        return sclRef.doName() == null ? null : sclRef;
    }

    /** Find an LN by LD instance + LN full name within the current IED. */
    protected static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}
