package com.ysh.jcms.utils.transport.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FrameAssembler — stateful reassembly of segmented frames.
 *
 * <p>
 * Segmented frames arrive with Next=true in the header. The assembler
 * accumulates them by ReqID until the final segment (Next=false) arrives, then
 * merges all segments into one complete Frame.
 */
public class FrameAssembler {

    /** Max distinct ReqIDs with pending segments; bounds the pending map. */
    private static final int MAX_PENDING_SEGMENTS = 1024;
    /** Max accumulated bytes of pending segments; a hard memory budget against DoS. */
    private static final long MAX_PENDING_BYTES = 8L * 1024 * 1024;

    private final Map<Integer, List<Frame>> pending = new HashMap<>();
    private long pendingBytes;

    /**
     * Add a received frame segment.
     *
     * @param segment
     *            the incoming (possibly incomplete) frame
     * @return the complete merged Frame, or null if more segments expected
     * @throws FrameFormatException
     *             on protocol violation
     */
    public synchronized Frame addSegment(Frame segment) throws FrameFormatException {
        int reqId = segment.reqId();

        if (!segment.header().next()) {
            List<Frame> previous = pending.remove(reqId);
            if (previous == null)
                return segment;
            pendingBytes -= sumAsduBytes(previous);
            previous.add(segment);
            return FrameCodec.merge(previous);
        }

        if (pending.containsKey(reqId)) {
            throw new FrameFormatException("ReqID " + reqId + " reused before previous completed");
        }
        if (pending.size() >= MAX_PENDING_SEGMENTS || pendingBytes + segment.asduBytes().length > MAX_PENDING_BYTES) {
            throw new FrameFormatException(
                    "Segmentation budget exceeded: pending=" + pending.size() + ", bytes=" + pendingBytes);
        }
        List<Frame> list = new ArrayList<>();
        list.add(segment);
        pending.put(reqId, list);
        pendingBytes += segment.asduBytes().length;
        return null;
    }

    /** Remove all pending segments for the given ReqID. */
    public synchronized void removePending(int reqId) {
        List<Frame> removed = pending.remove(reqId);
        if (removed != null)
            pendingBytes -= sumAsduBytes(removed);
    }

    private static int sumAsduBytes(List<Frame> frames) {
        int total = 0;
        for (Frame f : frames)
            total += f.asduBytes().length;
        return total;
    }

    /** Exception for frame format violations. */
    public static class FrameFormatException extends Exception {
        public FrameFormatException(String message) {
            super(message);
        }
    }
}
