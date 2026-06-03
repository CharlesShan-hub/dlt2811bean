package com.ysh.jcms.datatypes.compound;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CmsBinaryTime extends AbstractCmsCompound<CmsBinaryTime> {

    public static class NativeStruct extends Structure {
        public int msOfDay;
        public short daysSince1984;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("msOfDay", "daysSince1984");
        }
    }

    public int msOfDay;
    public int daysSince1984;

    public CmsBinaryTime() {
        super("BinaryTime");
        nativeStruct = new NativeStruct();
    }

    public CmsBinaryTime(int msOfDay, int daysSince1984) {
        this();
        this.msOfDay = msOfDay;
        this.daysSince1984 = daysSince1984;
    }

    public CmsBinaryTime(int hour, int minute, int second, int millisecond, int daysSince1984) {
        this(((hour * 60 + minute) * 60 + second) * 1000 + millisecond, daysSince1984);
    }

    private void syncToNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        ns.msOfDay = msOfDay;
        ns.daysSince1984 = (short) daysSince1984;
    }

    private void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        msOfDay = ns.msOfDay;
        daysSince1984 = ns.daysSince1984 & 0xFFFF;
    }

    public int getHour() { return msOfDay / 3600000; }
    public int getMinute() { return (msOfDay % 3600000) / 60000; }
    public int getSecond() { return (msOfDay % 60000) / 1000; }
    public int getMillisecond() { return msOfDay % 1000; }

    public byte[] encode() {
        syncToNative();
        write();
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_binary_time_encode((NativeStruct) nativeStruct, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBinaryTime decode(byte[] data) {
        CmsBinaryTime bt = new CmsBinaryTime();
        CmsFFIDatatypes.INSTANCE.cms_binary_time_decode(data, data.length, bt.nativeStruct);
        ((NativeStruct) bt.nativeStruct).read();
        bt.syncFromNative();
        return bt;
    }
}
