package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.node.InnerServer;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.Session;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * Base class for server-side service handlers with auto-decode and auto-error
 * support.
 *
 * <p>
 * {@link #handleRequest(Session, Frame)} is {@code final} — it auto-decodes the
 * request PDU and delegates to {@link #onDecodeSuccess(Session, CmsType)}.
 *
 * <p>
 * If an error PDU type is provided via constructor, the default
 * {@link #onDecodeError(int)} builds the error response automatically.
 * Subclasses may override for custom error logic.
 */
public abstract class BaseServerHandler extends BaseHandler implements ServiceHandler {

    private final ServiceName serviceName;
    private final Class<? extends CmsType> requestType;
    private final Class<? extends CmsType> errorType;

    /** Full constructor: request PDU + error PDU. */
    protected BaseServerHandler(ServiceName serviceName, Class<? extends CmsType> requestType, Class<? extends CmsType> errorType) {
        this.serviceName = serviceName;
        this.requestType = requestType;
        this.errorType = errorType;
    }

    /** For services with a request PDU but no distinct error PDU. */
    protected BaseServerHandler(ServiceName serviceName, Class<? extends CmsType> requestType) {
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
        CmsType decoded;
        if (requestType != null) {
            try {
                decoded = requestType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("Failed to instantiate {}", requestType.getSimpleName(), e);
                return onDecodeError(0, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
            prepareDecode(decoded);
            if (!tryDecode(session, request, decoded)) {
                return onDecodeError(0, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
            if (traceEnabled())
                log.info("[TRACE] <<< {} reqId={}:\n{}", serviceName, request.reqId(), decoded);
        } else {
            decoded = null;
        }
        Frame response = onDecodeSuccess(session, decoded);
        if (response != null && traceEnabled()) {
            log.info("[TRACE] >>> {} resp reqId={} err={} ({} bytes)", serviceName, response.reqId(), response.header().err(),
                    response.asduBytes() != null ? response.asduBytes().length : 0);
        }
        return response;
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
     *            the decoded request PDU, or {@code null} for PDU-less services;
     *            cast to the concrete type known by the subclass
     * @return response frame, or {@code null} for one-way messages
     */
    protected abstract Frame onDecodeSuccess(Session session, CmsType req);

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
            CmsType errorPdu = errorType.getDeclaredConstructor().newInstance();
            trySet(errorPdu, "reqId", reqId);
            trySet(errorPdu, "serviceError", err);
            return buildError(errorPdu.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to build error PDU via reflection", e);
            return buildError(new byte[]{0, 0, 0, 0}, reqId);
        }
    }

    @SuppressWarnings("unchecked")
    private static void trySet(CmsType target, String methodSuffix, int value) throws Exception {
        // Try reqId(int) or serviceError(int) setter pattern
        String methodName = methodSuffix; // e.g. "reqId" or "serviceError"
        try {
            Method m = target.getClass().getMethod(methodName, int.class);
            m.invoke(target, value);
        } catch (NoSuchMethodException e) {
            // ignore — some error types may not have the field
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

    /** Extract a String from a PER-decoded CmsUint8Array. */
    protected static String str(com.ysh.jcms.data.string.CmsUint8Array arr) {
        if (arr == null || arr.len == 0)
            return null;
        return new String(arr.value(), StandardCharsets.UTF_8);
    }

    /** Extract an optional String field controlled by a Present marker. */
    protected static String opt(com.ysh.jcms.data.scalar.CmsBoolean present, com.ysh.jcms.data.string.CmsUint8Array arr) {
        if (!present.value() || arr == null || arr.len == 0)
            return null;
        return new String(arr.value(), StandardCharsets.UTF_8);
    }

    // ──────────────────────────────────────────────
    // SCL model access
    // ──────────────────────────────────────────────

    protected com.ysh.jcms.utils.scl.SclDocument getScl2Document(Session session) {
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
        return CmsConfigLoader.load().getProtocol().getMaxArraySize();
    }

    private static boolean traceEnabled() {
        return CmsConfigLoader.load().getClient().getConsole().isTracePdu();
    }
}
