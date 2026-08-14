package com.ysh.jcms.app.handler.support;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.handler.base.BaseHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

/**
 * Encapsulates sending one request and decoding one response.
 * <p>
 * Used internally by {@link BaseClientHandler#send(CmsServiceInfo, BaseDao)} to
 * replace the raw {@code do-while} loop in the pagination path. Usage:
 *
 * <pre>
 * {
 *     &#64;code
 *     PaginationIterator<Frame> it = new PaginationIterator<>(node, sc, dao, frame -> frame, frame -> {
 *         onError(frame);
 *         return "";
 *     });
 *
 *     while (it.hasNext()) {
 *         Frame frame = it.next();
 *         // process frame ...
 *         it.requestNext(cursor); // continue to next page
 *     }
 * }
 * </pre>
 *
 * @param <T>
 *            the decoded response type
 */
public class PaginationIterator<T> {

    private final CmsNode node;
    private final CmsServiceInfo sc;
    private final BaseDao dao;
    private final Decoder<T> decoder;
    private final ErrorDecoder errorDecoder;

    private boolean alive = true;

    @FunctionalInterface
    public interface Decoder<T> {
        T decode(Frame frame) throws IOException;
    }

    @FunctionalInterface
    public interface ErrorDecoder {
        String decode(Frame frame) throws IOException;
    }

    /**
     * @param node
     *            the CMS node
     * @param sc
     *            the service info
     * @param dao
     *            the DAO (request builder)
     * @param decoder
     *            decodes a Frame into the response type
     * @param errorDecoder
     *            decodes an error frame into a human-readable message
     */
    public PaginationIterator(CmsNode node, CmsServiceInfo sc, BaseDao dao, Decoder<T> decoder, ErrorDecoder errorDecoder) {
        this.node = node;
        this.sc = sc;
        this.dao = dao;
        this.decoder = decoder;
        this.errorDecoder = errorDecoder;
    }

    /**
     * Returns {@code true} if more requests can be sent. Returns {@code false}
     * after an error, timeout, or explicit {@link #kill()}.
     */
    public boolean hasNext() {
        return alive;
    }

    /**
     * Sends one request and returns the decoded response.
     * <p>
     * Does NOT handle cursor advancement or loop control — that is the caller's
     * responsibility.
     */
    public T next() throws IOException {
        if (!alive)
            throw new IOException("PaginationIterator is exhausted");

        byte[] pdu = dao.toRequest().encode();
        BaseHandler.trace(">>>\n" + dao.toRequest());

        if (node == null) {
            alive = false;
            throw new IOException("PaginationIterator: node is null");
        }

        Frame frame = node.sendRequest(sc, pdu);
        if (frame == null) {
            alive = false;
            throw new IOException("Request timed out for " + sc);
        }

        if (frame.header().err()) {
            alive = false;
            String msg = errorDecoder.decode(frame);
            throw new IOException(msg);
        }

        return decoder.decode(frame);
    }

    /**
     * Signal that the next page should be fetched. If {@code cursor} is null or
     * empty, the iterator is exhausted.
     */
    public void requestNext(String cursor) {
        if (cursor != null && !cursor.isEmpty()) {
            alive = true;
        } else {
            alive = false;
        }
    }

    /** Exhaust the iterator (no more pages will be fetched). */
    public void kill() {
        alive = false;
    }
}
