package com.ysh.dlt2811bean.transport.protocol;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages CMS frame splitting and reassembly.
 *
 * <p><b>Sending side (split):</b>
 * When an ASDU exceeds the maximum frame size, it is split into multiple frames.
 * Each frame carries its own ReqID (PER-encoded). The first frame preserves any
 * PER prefix (optional bitmap) before the ReqID.
 *
 * <p><b>Receiving side (assembly):</b>
 * Incomplete frames are accumulated by ReqID. When the last frame (Next=0) arrives,
 * all segments are merged into a single ASDU, skipping duplicate ReqIDs from
 * subsequent segments.
 *
 * <p>Thread safety: {@link #addSegment} is synchronized; {@link #split} is stateless.
 */
public class CmsFrameManager {

    /**
     * Exception for frame format or protocol violations.
     * The connection should NOT be closed on this exception.
     */
    public static class FrameFormatException extends Exception {
        public FrameFormatException(String message) {
            super(message);
        }
    }

    /** ReqID → list of segments with Next=1 (incomplete, waiting for last). */
    private final Map<Integer, List<CmsApdu>> pending = new HashMap<>();

    /**
     * Adds a received segment and returns a complete APDU if all segments
     * have been received.
     *
     * @param segment a received (loaded but not decoded) APDU segment
     * @return the complete merged APDU, or null if more segments are expected
     * @throws FrameFormatException on protocol violation
     */
    public synchronized CmsApdu addSegment(CmsApdu segment) throws FrameFormatException {
        int reqId = segment.getReqId();

        if (!segment.getApch().isNext()) {
            List<CmsApdu> previous = pending.remove(reqId);
            if (previous == null) {
                return segment;
            }
            previous.add(segment);
            return merge(previous);
        }

        if (pending.containsKey(reqId)) {
            pending.remove(reqId);
            throw new FrameFormatException(
                "ReqID " + reqId + " reused before previous transfer completed");
        }
        List<CmsApdu> list = new ArrayList<>();
        list.add(segment);
        pending.put(reqId, list);
        return null;
    }

    /**
     * Merges a list of segments back into a single APDU by concatenating
     * their asduBytes in order.
     */
    public static CmsApdu merge(List<CmsApdu> segments) {
        CmsApdu last = segments.getLast();

        int totalLen = 0;
        for (CmsApdu seg : segments) {
            totalLen += seg.asduBytes.length;
        }

        byte[] merged = new byte[totalLen];
        int offset = 0;
        for (CmsApdu seg : segments) {
            System.arraycopy(seg.asduBytes, 0, merged, offset, seg.asduBytes.length);
            offset += seg.asduBytes.length;
        }

        last.asduBytes = merged;
        last.actualAsduSize = merged.length;
        last.apch.next(false);
        last.apch.frameLength(Math.min(merged.length, CmsApdu.MAX_ASDU_SIZE));
        last.segmented = false;
        return last;
    }

    /**
     * Splits a large APDU into multiple frames, using {@link CmsApdu#MAX_ASDU_SIZE}
     * as the maximum frame size.
     *
     * @param apdu the APDU to split (must have asduBytes already computed)
     * @return list of segments, or a singleton list if no split is needed
     */
    public static List<CmsApdu> split(CmsApdu apdu) {
        return split(apdu, CmsApdu.MAX_ASDU_SIZE);
    }

    /**
     * Splits a large APDU into multiple frames, each no larger than maxFrameSize.
     *
     * @param apdu         the APDU to split (must have asduBytes already computed)
     * @param maxFrameSize the maximum frame size (FL value) for each segment
     * @return list of segments, or a singleton list if no split is needed
     */
    public static List<CmsApdu> split(CmsApdu apdu, int maxFrameSize) {
        byte[] asduBytes = apdu.asduBytes;
        if (asduBytes == null) {
            throw new IllegalStateException("ASDU not encoded yet");
        }

        int totalLen = asduBytes.length;
        if (totalLen <= CmsApdu.MAX_ASDU_SIZE) {
            return List.of(apdu);
        }

        int maxChunk = Math.min(maxFrameSize, CmsApdu.MAX_ASDU_SIZE);

        List<CmsApdu> segments = new ArrayList<>();
        int offset = 0;
        while (offset < totalLen) {
            int chunkSize = Math.min(maxChunk, totalLen - offset);
            byte[] chunk = new byte[chunkSize];
            System.arraycopy(asduBytes, offset, chunk, 0, chunkSize);

            boolean isLast = (offset + chunkSize >= totalLen);

            CmsApdu segment = new CmsApdu();
            segment.asduBytes = chunk;
            segment.segmented = !isLast;
            segment.actualAsduSize = chunkSize;
            segment.reqId = apdu.reqId;
            segment.apch.fromMessageType(apdu.messageType);
            segment.apch.serviceCode(apdu.getAsdu().getServiceName());
            segment.apch.next(!isLast);
            segment.apch.frameLength(chunkSize);

            segments.add(segment);
            offset += chunkSize;
        }
        return segments;
    }
}
