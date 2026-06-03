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
public class CmsEntryTime extends AbstractCmsCompound<CmsEntryTime> {

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

    public CmsEntryTime() {
        super("EntryTime");
        nativeStruct = new NativeStruct();
    }

    public CmsEntryTime(int msOfDay, int daysSince1984) {
        this();
        this.msOfDay = msOfDay;
        this.daysSince1984 = daysSince1984;
    }

    public byte[] encode() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        ns.msOfDay = msOfDay;
        ns.daysSince1984 = (short) daysSince1984;
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_entry_time_encode(ns, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsEntryTime decode(byte[] data) {
        NativeStruct ns = new NativeStruct();
        CmsFFIDatatypes.INSTANCE.cms_entry_time_decode(data, data.length, ns);
        ns.read();
        return new CmsEntryTime(ns.msOfDay, ns.daysSince1984 & 0xFFFF);
    }
}
