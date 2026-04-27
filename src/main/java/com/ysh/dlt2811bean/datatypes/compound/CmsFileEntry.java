package com.ysh.dlt2811bean.datatypes.compound;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Setter;
import lombok.experimental.Accessors;

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
 * // Chain usage
 * CmsFileEntry entry = new CmsFileEntry()
 *     .fileName(new CmsVisibleString("report.txt").max(129))
 *     .fileSize(new CmsInt32U(1024))
 *     .lastModified(new CmsUtcTime(1715000000L, 0, 0L))
 *     .checkSum(new CmsInt32U(0xAABBCCDDL));
 *
 * // Quick mode
 * CmsFileEntry entry = new CmsFileEntry("report.txt", 1024, utcTime, 0xAABBCCDDL);
 *
 * // Encode / Decode
 * entry.encode(pos);
 * CmsFileEntry r = new CmsFileEntry().decode(pis);
 * </pre>
 */
@Setter
@Accessors(fluent = true)
public class CmsFileEntry extends AbstractCmsCompound<CmsFileEntry> {

    public CmsVisibleString fileName = new CmsVisibleString().max(129);
    public CmsInt32U fileSize = new CmsInt32U(0L);
    public CmsUtcTime lastModified = new CmsUtcTime();
    public CmsInt32U checkSum = new CmsInt32U(0L);

    public CmsFileEntry() {
        super("FileEntry");
        registerField("fileName");
        registerField("fileSize");
        registerField("lastModified");
        registerField("checkSum");
    }

    public CmsFileEntry(String fileName, long fileSize, CmsUtcTime lastModified, long checkSum) {
        this();
        this.fileName.set(fileName);
        this.fileSize.set(fileSize);
        this.lastModified = lastModified;
        this.checkSum.set(checkSum);
    }
}