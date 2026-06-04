package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CmsFileEntry extends AbstractCmsCompound<CmsFileEntry> {

    public String fileName;
    public long fileSize;
    public byte[] lastModified;
    public long checkSum;

    public CmsFileEntry() {
        super("FileEntry");
    }

    public CmsFileEntry(String fileName, long fileSize, byte[] lastModified, long checkSum) {
        this();
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
        this.checkSum = checkSum;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(); // flat FFI — no struct fields
    }

    @Override
    public byte[] encode() {
        syncToNative();
        byte[] buf = new byte[encodeBufSize()];
        IntByReference outLen = new IntByReference(buf.length);
        ffiEncode(buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsFileEntry decode(byte[] data) {
        ffiDecode(data);
        syncFromNative();
        return this;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_file_entry_encode(fileName, fileSize, lastModified, checkSum, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] fileNameBuf = new byte[256];
        IntByReference fileNameCap = new IntByReference(129);
        LongByReference fileSizeRef = new LongByReference();
        byte[] lastModifiedBuf = new byte[8];
        LongByReference checkSumRef = new LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_file_entry_decode(data, data.length,
                fileNameBuf, fileNameCap, fileSizeRef, lastModifiedBuf, checkSumRef);
        this.fileName = new String(fileNameBuf, 0, fileNameCap.getValue(), StandardCharsets.US_ASCII);
        this.fileSize = fileSizeRef.getValue();
        this.lastModified = lastModifiedBuf;
        this.checkSum = checkSumRef.getValue();
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    public static CmsFileEntry from(byte[] data) {
        return new CmsFileEntry().decode(data);
    }
}
