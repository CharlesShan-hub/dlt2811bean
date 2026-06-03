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

    @Override
    protected void syncToNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        ns.msOfDay = msOfDay;
        ns.daysSince1984 = (short) daysSince1984;
    }

    @Override
    protected void syncFromNative() {
        NativeStruct ns = (NativeStruct) nativeStruct;
        msOfDay = ns.msOfDay;
        daysSince1984 = ns.daysSince1984 & 0xFFFF;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_entry_time_encode((NativeStruct) nativeStruct, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFIDatatypes.INSTANCE.cms_entry_time_decode(data, data.length, nativeStruct);
        ((NativeStruct) nativeStruct).read();
        syncFromNative();
    }

    @Override
    protected int encodeBufSize() {
        return 16;
    }

    public static CmsEntryTime from(byte[] data) {
        return new CmsEntryTime().decode(data);
    }
}
