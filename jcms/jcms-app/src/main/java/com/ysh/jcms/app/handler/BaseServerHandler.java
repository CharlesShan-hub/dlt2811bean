package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.node.InnerServer;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Base class for server-side service handlers with auto-decode and auto-error
 * support.
 *
 * <p>
 * {@link #handleRequest(Session, Frame)} is {@code final} — it auto-decodes the
 * request PDU and delegates to
 * {@link #onDecodeSuccess(Session, CmsTypeOld, int)}.
 *
 * <p>
 * If an error PDU type is provided via constructor, the default
 * {@link #onDecodeError(int)} builds the error response automatically.
 * Subclasses may override for custom error logic.
 *
 * <p>
 * Subclasses may throw {@link ServiceException} from {@link #onDecodeSuccess} —
 * it will be caught and converted to an error frame by {@code handleRequest},
 * eliminating repetitive null-check boilerplate.
 */
public abstract class BaseServerHandler<R extends CmsType, E extends CmsType> extends BaseHandler implements ServiceHandler {

    private final ServiceName serviceName;
    private final Class<R> requestType;
    private final Class<E> errorType;

    /** Full constructor: request PDU + error PDU. */
    protected BaseServerHandler(ServiceName serviceName, Class<R> requestType, Class<E> errorType) {
        this.serviceName = serviceName;
        this.requestType = requestType;
        this.errorType = errorType;
    }

    /** For services with a request PDU but no distinct error PDU. */
    protected BaseServerHandler(ServiceName serviceName, Class<R> requestType) {
        this(serviceName, requestType, null);
    }

    /** For services without a request PDU (e.g. TestServer). */
    protected BaseServerHandler(ServiceName serviceName) {
        this(serviceName, null, null);
    }

    @Override
    public final ServiceName getServiceName() {
        return serviceName;
    }

    @Override
    public final Frame handleRequest(Session session, Frame request) {
        int reqId = request.reqId();
        CmsType decoded;
        if (requestType != null) {
            try {
                decoded = requestType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("Failed to instantiate {}", requestType.getSimpleName(), e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
            prepareDecode(decoded);
            if (!tryDecode(session, request, decoded)) {
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
            if (traceEnabled())
                log.info("[TRACE] <<< {} reqId={}:\n{}", serviceName, reqId, decoded);
        } else {
            decoded = null;
        }
        try {
            @SuppressWarnings("unchecked")
            Frame response = onDecodeSuccess(session, (R) decoded, reqId);
            if (response != null && traceEnabled()) {
                log.info("[TRACE] >>> {} resp reqId={} err={} ({} bytes)", serviceName, response.reqId(), response.header().err(),
                        response.asduBytes() != null ? response.asduBytes().length : 0);
            }
            return response;
        } catch (ServiceException e) {
            return onDecodeError(e.reqId(), e.serviceError());
        }
    }

    /**
     * Hook called before decoding the request PDU.
     *
     * so that the native decoder can read the correct number of items.
     *
     * <p>
     * Default implementation does nothing.
     */
    protected void prepareDecode(CmsType decoded) {
    }

    /**
     * Process a successfully decoded request.
     *
     * @param session
     *            the session context
     * @param req
     *            the decoded request PDU, or {@code null} for PDU-less services
     * @param reqId
     *            request ID extracted from the frame header
     * @return response frame, or {@code null} for one-way messages
     * @throws ServiceException
     *             to abort processing and return an error frame
     */
    protected abstract Frame onDecodeSuccess(Session session, R req, int reqId);

    /**
     * Build an error response frame.
     *
     * <p>
     * Default implementation uses reflection to set {@code reqId} and
     * {@code serviceError} on the configured error type. Subclasses may override
     * for custom error PDU construction.
     *
     * @param reqId
     *            request ID
     * @param err
     *            service error code (e.g. {@link CmsServiceError#INSTANCE_IN_USE})
     */
    protected Frame onDecodeError(int reqId, int err) {
        if (errorType == null) {
            return buildError(new byte[]{0, 0, 0, 0}, reqId);
        }
        try {
            CmsType errorPdu;
            try {
                // New-style error PDUs carry the error code in the constructor, e.g. new
                // CmsGetDataValuesError(int)
                errorPdu = errorType.getDeclaredConstructor(int.class).newInstance(err);
            } catch (NoSuchMethodException e) {
                errorPdu = errorType.getDeclaredConstructor().newInstance();
            }
            return buildError(errorPdu.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to build error PDU via reflection", e);
            return buildError(new byte[]{0, 0, 0, 0}, reqId);
        }
    }

    // ──────────────────────────────────────────────
    // Response builders
    // ──────────────────────────────────────────────

    protected Frame buildSuccess(byte[] respBytes, int reqId) {
        return new Frame(new FrameHeader().serviceCode(getServiceName()).resp(true).err(false), respBytes, reqId);
    }

    protected Frame buildError(byte[] respBytes, int reqId) {
        return new Frame(new FrameHeader().serviceCode(getServiceName()).resp(true).err(true), respBytes, reqId);
    }

    protected static Frame noResponse() {
        return null;
    }

    /**
     * Convenience: encode a response PDU and wrap in a success frame. Auto-catches
     * encoding exceptions and returns an error frame instead.
     */
    protected Frame ok(CmsType resp, int reqId) {
        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode {} response", resp.getClass().getSimpleName(), e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    // ──────────────────────────────────────────────
    // String extraction helpers
    // ──────────────────────────────────────────────

    /** Extract a non-empty String from a PER-decoded byte array. */
    protected static String str(byte[] arr) {
        if (arr == null || arr.length == 0)
            return null;
        return new String(arr, StandardCharsets.UTF_8);
    }

    /** Extract a String from a CmsString value. */
    protected static String str(com.ysh.jcms.data.scalar.CmsString s) {
        return s == null ? null : s.value();
    }

    /** Extract a String from a CmsObjectReference value. */
    protected static String str(com.ysh.jcms.data.scalar.CmsObjectReference r) {
        return r == null ? null : r.value();
    }

    // ──────────────────────────────────────────────
    // SCL model access
    // ──────────────────────────────────────────────

    /** Resolve SCL document from session, or throw if unavailable. */
    protected SclDocument requireScl(Session session, int reqId) {
        SclDocument doc = getSclDocument(session);
        if (doc == null)
            throw new ServiceException(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return doc;
    }

    /** Resolve SCL IED from session, or throw if unavailable. */
    protected com.ysh.jcms.utils.scl.model.ied.SclIED requireIed(Session session, int reqId) {
        com.ysh.jcms.utils.scl.model.ied.SclIED ied = getSclIed(session);
        if (ied == null)
            throw new ServiceException(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return ied;
    }

    protected SclDocument getSclDocument(Session session) {
        try {
            return ((InnerServer.ServerSession) session).getSclDocument();
        } catch (ClassCastException e) {
            return null;
        }
    }

    protected com.ysh.jcms.utils.scl.model.ied.SclIED getSclIed(Session session) {
        try {
            return ((InnerServer.ServerSession) session).getSclIed();
        } catch (ClassCastException e) {
            return null;
        }
    }

    // ──────────────────────────────────────────────
    // FC code helper
    // ──────────────────────────────────────────────

    /** Map FC integer value to its string code (e.g. 0 → "ST", 1 → "MX"). */
    protected static String fcCode(int fcVal) {
        if (fcVal < 0 || fcVal >= FunctionalConstraint.values().length)
            return null;
        String code = FunctionalConstraint.values()[fcVal].name();
        return "XX".equals(code) ? null : code;
    }

    // ──────────────────────────────────────────────
    // Decode helper
    // ──────────────────────────────────────────────

    protected boolean tryDecode(Session session, Frame request, CmsType pdu) {
        try {
            pdu.decode(request.asduBytes());
            return true;
        } catch (Exception e) {
            log.error("Failed to decode {} from {}: {}", pdu.getClass().getSimpleName(), session.getSessionId(), e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────
    // Misc helpers
    // ──────────────────────────────────────────────

    /**
     * Maximum number of elements to return in one response page. Used by directory
     * services for pagination.
     */
    protected static int pageSize() {
        return CmsConfigLoader.load().protocol().maxArraySize();
    }

    /**
     * Apply {@code refAfter} pagination to a sorted list of reference strings.
     * Returns the sublist starting <em>after</em> the matched entry.
     *
     * @throws ServiceException
     *             with {@code INSTANCE_NOT_AVAILABLE} if {@code refAfter} is not
     *             found in the list
     */
    protected static List<String> after(List<String> items, String refAfter, int reqId) {
        if (refAfter == null || refAfter.isEmpty())
            return items;
        int idx = items.indexOf(refAfter);
        if (idx < 0)
            throw new ServiceException(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        return items.subList(idx + 1, items.size());
    }

    private static boolean traceEnabled() {
        return CmsConfigLoader.load().client().console().tracePdu();
    }
}
