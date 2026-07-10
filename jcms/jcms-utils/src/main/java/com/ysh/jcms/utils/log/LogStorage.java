package com.ysh.jcms.utils.log;

import com.ysh.jcms.data.block.CmsReasonCode;
import com.ysh.jcms.svc.log.CmsLogDataEntry;
import com.ysh.jcms.svc.log.CmsLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志持久化存储 — 基于文件的简单实现。
 *
 * <p>
 * 每个 LCB ref 一个目录，目录下文件：
 *
 * <pre>
 *   {rootPath}/{sanitizedRef}/
 *       log.dat     — 日志条目（追加写入）
 *       meta.dat    — 元数据（oldest/newest entry 信息）
 * </pre>
 *
 * <p>
 * log.dat 格式（每条 entry 连续写入）：
 *
 * <pre>
 *   [8 bytes] timeMsOfDay      (int64, big-endian)
 *   [4 bytes] timeDaysSince1984 (int32, big-endian)
 *   [8 bytes] entryId           (fixed OCTET STRING)
 *   [4 bytes] numDataEntries    (int32, big-endian)
 *   对于每个 data entry:
 *     [2 bytes] refLen            (int16, big-endian)
 *     [N bytes] reference         (UTF-8 bytes)
 *     [1 byte]  fc                (functional constraint value)
 *     [4 bytes] valueLen          (int32, big-endian)
 *     [N bytes] value             (PER-encoded CmsData bytes)
 *     [6 bytes] reasonCode        (6 boolean bits, 1 byte each)
 * </pre>
 *
 * meta.dat 格式：
 *
 * <pre>
 *   [8 bytes] oldestTimeMsOfDay
 *   [4 bytes] oldestTimeDaysSince1984
 *   [8 bytes] oldestEntryId
 *   [8 bytes] newestTimeMsOfDay
 *   [4 bytes] newestTimeDaysSince1984
 *   [8 bytes] newestEntryId
 * </pre>
 */
public class LogStorage {

    private static final Logger log = LoggerFactory.getLogger(LogStorage.class);

    private final Path rootPath;

    public LogStorage(String rootPath) {
        this.rootPath = Paths.get(rootPath);
        try {
            Files.createDirectories(this.rootPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create log root: " + rootPath, e);
        }
    }

    // ──────────────────────────────────────────────
    // 公共查询 API
    // ──────────────────────────────────────────────

    /**
     * 按时间查询日志条目。
     *
     * @param logRef
     *            LCB 引用（如 "LD0/LLN0.lcblog"）
     * @param startTime
     *            起始时间戳（毫秒），null 表示不限
     * @param stopTime
     *            结束时间戳（毫秒），null 表示不限
     * @param afterEntryId
     *            起始条目 ID（返回该条目之后的），null 表示从头
     * @param maxCount
     *            最多返回条数
     * @return 匹配的日志条目列表
     */
    public List<CmsLogEntry> queryByTime(String logRef, Long startTime, Long stopTime, String afterEntryId, int maxCount) {
        List<CmsLogEntry> result = new ArrayList<>();
        Path dir = refDir(logRef);
        Path dataFile = dir.resolve("log.dat");
        if (!Files.exists(dataFile))
            return result;

        boolean skipUntilAfter = (afterEntryId != null && !afterEntryId.isEmpty());

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFile.toFile()), 65536))) {
            while (in.available() > 0 && result.size() < maxCount) {
                long timeMsOfDay = in.readLong();
                int timeDaysSince = in.readInt();
                byte[] entryIdBytes = new byte[8];
                in.readFully(entryIdBytes);
                String entryIdStr = new String(entryIdBytes, StandardCharsets.UTF_8).trim();

                int numDataEntries = in.readInt();

                // 计算时间戳（毫秒）
                long entryTimeMs = (long) timeDaysSince * 86400000L + timeMsOfDay;

                // 跳过直到 afterEntry
                if (skipUntilAfter) {
                    if (entryIdStr.equals(afterEntryId.trim())) {
                        skipUntilAfter = false;
                    }
                    skipDataEntries(in, numDataEntries);
                    continue;
                }

                // 时间过滤
                if (startTime != null && entryTimeMs < startTime) {
                    skipDataEntries(in, numDataEntries);
                    continue;
                }
                if (stopTime != null && entryTimeMs > stopTime) {
                    skipDataEntries(in, numDataEntries);
                    continue;
                }

                // 读取 data entries
                CmsLogEntry entry = readEntry(in, timeMsOfDay, timeDaysSince, entryIdBytes, numDataEntries);
                if (entry != null) {
                    result.add(entry);
                }
            }
        } catch (EOFException e) {
            // 正常读到文件尾
        } catch (IOException e) {
            log.warn("Failed to read log data for ref={}: {}", logRef, e.getMessage());
        }
        return result;
    }

    /**
     * 查询指定条目之后的日志（同 queryByTime + afterEntryId）。
     */
    public List<CmsLogEntry> queryAfter(String logRef, String entryId, Long startTime, int maxCount) {
        return queryByTime(logRef, startTime, null, entryId, maxCount);
    }

    /**
     * 获取日志状态（最旧 / 最新条目信息）。
     */
    public LogStatus getStatus(String logRef) {
        Path dir = refDir(logRef);
        Path metaFile = dir.resolve("meta.dat");
        if (!Files.exists(metaFile)) {
            return LogStatus.EMPTY;
        }
        try (DataInputStream in = new DataInputStream(new FileInputStream(metaFile.toFile()))) {
            long oldestTimeMsOfDay = in.readLong();
            int oldestTimeDays = in.readInt();
            byte[] oldestEntryId = new byte[8];
            in.readFully(oldestEntryId);

            long newestTimeMsOfDay = in.readLong();
            int newestTimeDays = in.readInt();
            byte[] newestEntryId = new byte[8];
            in.readFully(newestEntryId);

            return new LogStatus(oldestTimeMsOfDay, oldestTimeDays, oldestEntryId, newestTimeMsOfDay, newestTimeDays, newestEntryId);
        } catch (IOException e) {
            log.warn("Failed to read meta for ref={}: {}", logRef, e.getMessage());
            return LogStatus.EMPTY;
        }
    }

    /**
     * 追加一条日志条目。
     */
    public synchronized void append(String logRef, CmsLogEntry entry) throws IOException {
        Path dir = ensureRefDir(logRef);
        Path dataFile = dir.resolve("log.dat");
        Path metaFile = dir.resolve("meta.dat");

        // 用 ByteArrayOutputStream 构建 entry 字节
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
        DataOutputStream dos = new DataOutputStream(bos);

        // timeOfEntry
        long msOfDay = entry.timeOfEntry.msOfDay.value() & 0xFFFFFFFFL;
        int days = entry.timeOfEntry.daysSince1984.value() & 0xFFFF;
        dos.writeLong(msOfDay);
        dos.writeInt(days);

        // entryId (固定 8 字节)
        byte[] eid = entry.entryId.value();
        if (eid == null)
            eid = new byte[8];
        byte[] eidPadded = new byte[8];
        System.arraycopy(eid, 0, eidPadded, 0, Math.min(eid.length, 8));
        dos.write(eidPadded);

        // data entries
        int numData = entry.entryData.items.size();
        dos.writeInt(numData);
        for (int i = 0; i < numData; i++) {
            CmsLogDataEntry de = entry.entryData.items.get(i);
            writeDataEntry(dos, de);
        }

        dos.flush();
        byte[] entryBytes = bos.toByteArray();

        // 追加写入 log.dat
        try (FileOutputStream fos = new FileOutputStream(dataFile.toFile(), true)) {
            fos.write(entryBytes);
            fos.flush();
        }

        // 更新 meta.dat
        updateMeta(metaFile, msOfDay, days, eidPadded);
    }

    // ──────────────────────────────────────────────
    // 内部方法
    // ──────────────────────────────────────────────

    private void writeDataEntry(DataOutputStream dos, CmsLogDataEntry de) throws IOException {
        // reference
        byte[] refBytes = de.reference.value();
        if (refBytes == null)
            refBytes = new byte[0];
        dos.writeShort(refBytes.length);
        dos.write(refBytes);

        // fc
        dos.writeByte(de.fc.value() & 0xFF);

        // value (PER encode)
        byte[] valBytes;
        try {
            valBytes = de.value.encode();
        } catch (Exception e) {
            valBytes = new byte[0];
        }
        dos.writeInt(valBytes.length);
        dos.write(valBytes);

        // reasonCode (6 boolean bits)
        writeReasonCode(dos, de.reason);
    }

    private static void writeReasonCode(DataOutputStream dos, CmsReasonCode rc) throws IOException {
        dos.writeBoolean(rc.data_change.value());
        dos.writeBoolean(rc.quality_change.value());
        dos.writeBoolean(rc.data_update.value());
        dos.writeBoolean(rc.integrity.value());
        dos.writeBoolean(rc.general_interrogation.value());
        dos.writeBoolean(rc.application_trigger.value());
    }

    private CmsLogEntry readEntry(DataInputStream in, long timeMsOfDay, int timeDaysSince, byte[] entryIdBytes, int numDataEntries)
            throws IOException {
        CmsLogEntry entry = new CmsLogEntry();

        entry.timeOfEntry.msOfDay.value(timeMsOfDay);
        entry.timeOfEntry.daysSince1984.value(timeDaysSince);
        entry.entryId.value(entryIdBytes);

        for (int i = 0; i < numDataEntries; i++) {
            CmsLogDataEntry de = readDataEntry(in);
            if (de != null) {
                entry.entryData.add(de);
            }
        }
        entry.entryData.count = entry.entryData.items.size();
        return entry;
    }

    private CmsLogDataEntry readDataEntry(DataInputStream in) throws IOException {
        CmsLogDataEntry de = new CmsLogDataEntry();

        // reference
        int refLen = in.readShort() & 0xFFFF;
        byte[] refBytes = new byte[refLen];
        in.readFully(refBytes);
        de.reference.value(refBytes);

        // fc
        de.fc.value(in.readByte() & 0xFF);

        // value (PER decode)
        int valLen = in.readInt();
        byte[] valBytes = new byte[valLen];
        in.readFully(valBytes);
        try {
            de.value.decode(valBytes);
        } catch (Exception e) {
            log.warn("Failed to decode LogDataEntry value, skipping: {}", e.getMessage());
        }

        // reasonCode (6 boolean bits)
        readReasonCode(in, de.reason);

        return de;
    }

    private static void readReasonCode(DataInputStream in, CmsReasonCode rc) throws IOException {
        rc.data_change.value(in.readBoolean());
        rc.quality_change.value(in.readBoolean());
        rc.data_update.value(in.readBoolean());
        rc.integrity.value(in.readBoolean());
        rc.general_interrogation.value(in.readBoolean());
        rc.application_trigger.value(in.readBoolean());
    }

    private void skipDataEntries(DataInputStream in, int num) throws IOException {
        for (int i = 0; i < num; i++) {
            int refLen = in.readShort() & 0xFFFF;
            in.skipBytes(refLen);
            in.readByte(); // fc
            int valLen = in.readInt();
            in.skipBytes(valLen);
            in.skipBytes(6); // reasonCode (6 booleans = 6 bytes)
        }
    }

    private synchronized void updateMeta(Path metaFile, long msOfDay, int days, byte[] entryId) {
        try {
            LogStatus existing = LogStatus.EMPTY;
            if (Files.exists(metaFile)) {
                existing = getStatus(metaFile.getParent().getFileName().toString());
            }

            if (existing == LogStatus.EMPTY || (existing.newestTimeMsOfDay == 0 && existing.newestTimeDays == 0)) {
                // 第一条：oldest = newest = 当前
                ByteArrayOutputStream bos = new ByteArrayOutputStream(36);
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeLong(msOfDay);
                dos.writeInt(days);
                dos.write(entryId);
                dos.writeLong(msOfDay);
                dos.writeInt(days);
                dos.write(entryId);
                dos.flush();
                Files.write(metaFile, bos.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                // 更新 newest
                ByteArrayOutputStream bos = new ByteArrayOutputStream(36);
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeLong(existing.oldestTimeMsOfDay);
                dos.writeInt(existing.oldestTimeDays);
                dos.write(existing.oldestEntryId);
                dos.writeLong(msOfDay);
                dos.writeInt(days);
                dos.write(entryId);
                dos.flush();
                Files.write(metaFile, bos.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            log.warn("Failed to update meta: {}", e.getMessage());
        }
    }

    /** 标准化 ref 为合法目录名 */
    private static String sanitizeRef(String logRef) {
        return logRef.replace('/', '_').replace('\\', '_').replace(':', '_');
    }

    private Path refDir(String logRef) {
        return rootPath.resolve(sanitizeRef(logRef));
    }

    private Path ensureRefDir(String logRef) throws IOException {
        Path dir = refDir(logRef);
        Files.createDirectories(dir);
        return dir;
    }

    // ──────────────────────────────────────────────
    // 日志状态值
    // ──────────────────────────────────────────────

    public static class LogStatus {
        public static final LogStatus EMPTY = new LogStatus(0, 0, new byte[8], 0, 0, new byte[8]);

        public final long oldestTimeMsOfDay;
        public final int oldestTimeDays;
        public final byte[] oldestEntryId;
        public final long newestTimeMsOfDay;
        public final int newestTimeDays;
        public final byte[] newestEntryId;

        public LogStatus(long oldestTimeMsOfDay, int oldestTimeDays, byte[] oldestEntryId, long newestTimeMsOfDay, int newestTimeDays,
                byte[] newestEntryId) {
            this.oldestTimeMsOfDay = oldestTimeMsOfDay;
            this.oldestTimeDays = oldestTimeDays;
            this.oldestEntryId = oldestEntryId;
            this.newestTimeMsOfDay = newestTimeMsOfDay;
            this.newestTimeDays = newestTimeDays;
            this.newestEntryId = newestEntryId;
        }
    }
}
