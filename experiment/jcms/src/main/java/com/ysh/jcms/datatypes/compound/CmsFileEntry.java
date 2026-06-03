package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;

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

    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_file_entry_encode(fileName, fileSize, lastModified, checkSum, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsFileEntry decode(byte[] data) {
        byte[] fileNameBuf = new byte[256];
        IntByReference fileNameCap = new IntByReference(129);
        LongByReference fileSize = new LongByReference();
        byte[] lastModified = new byte[8];
        LongByReference checkSum = new LongByReference();
        CmsFFIDatatypes.INSTANCE.cms_file_entry_decode(data, data.length,
                fileNameBuf, fileNameCap, fileSize, lastModified, checkSum);
        String fileName = new String(fileNameBuf, 0, fileNameCap.getValue(), StandardCharsets.US_ASCII);
        return new CmsFileEntry(fileName, fileSize.getValue(), lastModified, checkSum.getValue());
    }
}
