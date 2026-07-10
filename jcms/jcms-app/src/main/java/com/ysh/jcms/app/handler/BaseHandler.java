package com.ysh.jcms.app.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Root base class for all service handlers (client and server).
 *
 * <p>
 * Provides the shared {@link Logger} instance.
 */
public abstract class BaseHandler {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Extract the 16-bit reqId from the first two bytes of the encoded PDU.
     */
    protected static int reqIdFromBytes(byte[] pduBytes) {
        return (pduBytes[0] & 0xFF) << 8 | (pduBytes[1] & 0xFF);
    }
}
