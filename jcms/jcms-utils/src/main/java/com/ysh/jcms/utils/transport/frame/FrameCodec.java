package com.ysh.jcms.utils.transport.frame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * FrameCodec — stateless frame encoding, decoding, and splitting.
 *
 * <p>
 * Wire format (APDU = APCH + ASDU, ASDU = ReqID + Data, 16-bit fields
 * little-endian):
 *
 * <pre>
 * [APCH:4][ReqID:2][Data:FL-2]   where APCH = [CC][SC][FL(lo)][FL(hi)]
 * </pre>
 *
 * FL is the ASDU length (ReqID + Data), excluding the 4-byte APCH, per DL/T
 * 2811 6.1.2 c). FL = 0 for a header-only frame (Test), FL = 2 for an empty
 * data area.
 */
public class FrameCodec {

    public static final int REQID_SIZE = 2;

    private FrameCodec() {
    }

    public static byte[] encode(Frame frame) throws IOException {
        // Header-only frames (Test: reqId=0, empty payload) carry no ReqID, FL=0, per
        // DL/T 2811 6.3
        boolean headerOnly = frame.reqId() == 0 && frame.asduBytes().length == 0;
        int fl = headerOnly ? 0 : REQID_SIZE + frame.asduBytes().length; // excludes APCH, per standard
        frame.header().frameLength(fl);
        byte[] hdr = frame.header().encode();

        ByteArrayOutputStream bos = new ByteArrayOutputStream(hdr.length + (headerOnly ? 0 : REQID_SIZE) + frame.asduBytes().length);
        bos.write(hdr);
        if (!headerOnly) {
            int reqId = frame.reqId();
            bos.write(reqId & 0xFF); // ReqID low byte first
            bos.write((reqId >> 8) & 0xFF);
        }
        bos.write(frame.asduBytes());
        return bos.toByteArray();
    }

    /**
     * Split an ASDU into segments, per DL/T 2811 6.5.1: cut the data area
     * (excluding ReqID).
     */
    public static List<Frame> split(Frame frame, int maxPayloadSize) {
        byte[] asdu = frame.asduBytes();
        if (maxPayloadSize <= 0) {
            throw new IllegalArgumentException("maxPayloadSize must be > 0");
        }
        if (asdu.length <= maxPayloadSize)
            return Collections.singletonList(frame);

        int offset = 0;
        int segCount = (asdu.length + maxPayloadSize - 1) / maxPayloadSize;
        Frame[] segments = new Frame[segCount];
        for (int i = 0; i < segCount; i++) {
            int chunkLen = Math.min(maxPayloadSize, asdu.length - offset);
            boolean isLast = (offset + chunkLen >= asdu.length);
            byte[] chunk = new byte[chunkLen];
            System.arraycopy(asdu, offset, chunk, 0, chunkLen);

            FrameHeader segHdr = new FrameHeader().resp(frame.header().resp()).err(frame.header().err())
                    .serviceCode(frame.header().serviceCode()).next(!isLast); // 6.5.2: Next=1 means more to come

            segments[i] = new Frame(segHdr, chunk, frame.reqId()); // 6.5.1 b): same ReqID as the original ASDU
            offset += chunkLen;
        }
        return Arrays.asList(segments);
    }

    /**
     * Reassemble segments into one frame, per DL/T 2811 6.5.2 (sum of the data
     * areas).
     */
    public static Frame merge(List<Frame> segments) {
        if (segments.isEmpty())
            throw new IllegalArgumentException("No segments to merge");
        if (segments.size() == 1)
            return segments.get(0);

        Frame last = segments.get(segments.size() - 1);
        int total = 0;
        for (Frame seg : segments)
            total += seg.asduBytes().length;

        byte[] merged = new byte[total];
        int pos = 0;
        for (Frame seg : segments) {
            System.arraycopy(seg.asduBytes(), 0, merged, pos, seg.asduBytes().length);
            pos += seg.asduBytes().length;
        }
        return new Frame(last.header(), merged, last.reqId());
    }
}
