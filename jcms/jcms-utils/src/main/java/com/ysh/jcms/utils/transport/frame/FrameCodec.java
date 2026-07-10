package com.ysh.jcms.utils.transport.frame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * FrameCodec — stateless frame encoding, decoding, and splitting.
 *
 * <p>
 * Wire format:
 *
 * <pre>
 * [FL:2][FrameHeader:4][ASDU:FL-4]
 * </pre>
 */
public class FrameCodec {

    private FrameCodec() {
    }

    public static byte[] encode(Frame frame) throws IOException {
        int fl = FrameHeader.HEADER_SIZE + frame.asduBytes().length;
        frame.header().frameLength(fl);
        byte[] hdr = frame.header().encode();

        ByteArrayOutputStream bos = new ByteArrayOutputStream(2 + hdr.length + frame.asduBytes().length);
        bos.write((fl >> 8) & 0xFF);
        bos.write(fl & 0xFF);
        bos.write(hdr);
        bos.write(frame.asduBytes());
        return bos.toByteArray();
    }

    public static Frame decode(byte[] data, int offset) {
        if (data.length - offset < 6)
            throw new IllegalArgumentException("Frame too short");

        int fl = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
        FrameHeader header = FrameHeader.decode(data, offset + 2);
        int asduOff = offset + 2 + FrameHeader.HEADER_SIZE;
        int asduLen = fl - FrameHeader.HEADER_SIZE;

        if (asduLen < 0 || asduOff + asduLen > data.length) {
            throw new IllegalArgumentException("Invalid frame: fl=" + fl + ", dataLen=" + data.length);
        }
        return new Frame(header, Arrays.copyOfRange(data, asduOff, asduOff + asduLen));
    }

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
                    .serviceCode(frame.header().serviceCode()).next(!isLast);

            segments[i] = new Frame(segHdr, chunk, frame.reqId());
            offset += chunkLen;
        }
        return Arrays.asList(segments);
    }

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
