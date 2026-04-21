package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.ysh.dlt2811bean.utils.per.data.CmsVisibleString.Mode;

/**
 * DL/T 2811 file entry type (§7.3.10, Table 11).
 *
 * <pre>
 * ┌──────────────┬───────────────────┬────────────────────┬───────────┐
 * │ Field        │ 2811 Type         │ Constraints        │ Java type │
 * ├──────────────┼───────────────────┼────────────────────┼───────────┤
 * │ fileName     │ VisibleString     │ SIZE(0..129)       │ String    │
 * │ fileSize     │ INT32U            │ —                  │ long      │
 * │ lastModified │ UtcTime           │ —                  │ CmsUtcTime│
 * │ checkSum     │ INT32U            │ —                  │ long      │
 * └──────────────┴───────────────────┴────────────────────┴───────────┘
 * </pre>
 *
 * <p>FileName should not contain path. Trailing "/" indicates subdirectory.
 * CheckSum is CRC32 with polynomial 0x04C11DB7.
 *
 * <pre>
 * // Create
 * CmsFileEntry entry = new CmsFileEntry();
 * entry.setFileName("report.txt")
 *      .setFileSize(1024)
 *      .setLastModified(new CmsUtcTime(...))
 *      .setCheckSum(0xAABBCCDD);
 *
 * // Encode / Decode
 * CmsFileEntry.encode(pos, entry);
 * CmsFileEntry r = CmsFileEntry.decode(pis);
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
public final class CmsFileEntry {

    private String fileName;
    private long fileSize;
    private CmsUtcTime lastModified;
    private long checkSum;

    public CmsFileEntry() {}

    // ==================== Encode / Decode ====================

    public static void encode(PerOutputStream pos, CmsFileEntry value) {
        CmsVisibleString.encode(pos, value.fileName != null ? value.fileName : "", Mode.VARIABLE, 129);
        PerInteger.encode(pos, value.fileSize & 0xFFFFFFFFL, 0, 4294967295L);
        CmsUtcTime.encode(pos, value.lastModified);
        PerInteger.encode(pos, value.checkSum & 0xFFFFFFFFL, 0, 4294967295L);
    }

    public static CmsFileEntry decode(PerInputStream pis) throws PerDecodeException {
        CmsFileEntry entry = new CmsFileEntry();
        entry.fileName = CmsVisibleString.decode(pis, Mode.VARIABLE, 129).getValue();
        entry.fileSize = PerInteger.decode(pis, 0, 4294967295L);
        entry.lastModified = CmsUtcTime.decode(pis);
        entry.checkSum = PerInteger.decode(pis, 0, 4294967295L);
        return entry;
    }

    @Override
    public String toString() {
        return String.format("FileEntry[name=%s, size=%d, crc=0x%08X]",
                fileName, fileSize, checkSum);
    }
}
