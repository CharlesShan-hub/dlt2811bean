package com.ysh.jcms.app.handler.support;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.handler.base.BaseHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

/**
 * Encapsulates a single request-response exchange: a "drawer" that holds
 * everything needed to send one request and collect its response.
 * <p>
 * Both the single-shot path and the pagination loop in
 * {@link BaseClientHandler} go through {@link #exchange()}, so the
 * send/trace/error handling logic lives in exactly one place.
 */
public final class RequestExchange {

    @FunctionalInterface
    public interface ErrorDecoder {
        String decode(Frame frame) throws IOException;
    }

    private final CmsNode node;
    private final CmsServiceInfo sc;
    private final BaseDao dao;
    private final ErrorDecoder errorDecoder;

    /**
     * @param node
     *            the CMS node used to send the request
     * @param sc
     *            the service info (service code)
     * @param dao
     *            the DAO whose {@code toRequest()} builds the request PDU
     * @param errorDecoder
     *            decodes a negative response frame into a human-readable message
     */
    public RequestExchange(CmsNode node, CmsServiceInfo sc, BaseDao dao, ErrorDecoder errorDecoder) {
        this.node = node;
        this.sc = sc;
        this.dao = dao;
        this.errorDecoder = errorDecoder;
    }

    /**
     * Send one request (built from the DAO) and return the response frame.
     * <p>
     * Re-running this method sends a fresh request; the DAO may be mutated in
     * between (e.g. advancing the pagination cursor) to change the request.
     *
     * @return the response frame
     * @throws IOException
     *             on timeout, negative response, or transport failure
     */
    public Frame exchange() throws IOException {
        byte[] pdu = dao.toRequest().encode();
        BaseHandler.trace(">>>\n" + dao.toRequest());

        if (node == null) {
            throw new IOException("RequestExchange: node is null");
        }

        Frame frame = node.sendRequest(sc, pdu);
        if (frame == null) {
            throw new IOException("Request timed out for " + sc);
        }

        if (frame.header().err()) {
            String msg = errorDecoder.decode(frame);
            throw new IOException(msg);
        }

        return frame;
    }
}
