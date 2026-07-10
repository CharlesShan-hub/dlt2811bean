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

    private final Map<Integer, List<Frame>> pending = new HashMap<>();

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
            previous.add(segment);
            return FrameCodec.merge(previous);
        }

        if (pending.containsKey(reqId)) {
            throw new FrameFormatException("ReqID " + reqId + " reused before previous completed");
        }
        List<Frame> list = new ArrayList<>();
        list.add(segment);
        pending.put(reqId, list);
        return null;
    }

    /** Remove all pending segments for the given ReqID. */
    public synchronized void removePending(int reqId) {
        pending.remove(reqId);
    }

    /** Exception for frame format violations. */
    public static class FrameFormatException extends Exception {
        public FrameFormatException(String message) {
            super(message);
        }
    }
}
