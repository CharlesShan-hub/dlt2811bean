package com.ysh.jcms.app.tool;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.log.CmsLogDataEntry;
import com.ysh.jcms.data.sequence.log.CmsLogEntry;
import com.ysh.jcms.utils.log.LogStorage;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 模拟日志生成器 — 生成测试用的日志文件。
 *
 * <p>
 * 直接运行 main 方法：
 *
 * <pre>
 *   mvn -q exec:java -pl jcms-app "-Dexec.mainClass=com.ysh.jcms.app.tool.MockLogGenerator"
 * </pre>
 *
 * <p>
 * 参数： 参数1: 日志存储根路径（默认 config/logs） 参数2: 生成条数（默认 10）
 */
public class MockLogGenerator {

    private static final String LOG_REF = "LD0/LLN0.lcblog";
    private static final String[] DATASET_REFS = {"LD0/LLN0.Mod.stVal", "LD0/LLN0.Beh.stVal", "LD0/LLN0.Health.stVal",};

    public static void main(String[] args) throws Exception {
        String rootPath = args.length > 0 ? args[0] : "config/logs";
        int numRecords = args.length > 1 ? Integer.parseInt(args[1]) : 10;

        LogStorage storage = new LogStorage(rootPath);
        String dirName = LOG_REF.replace('/', '_');

        System.out.println("Generating " + numRecords + " mock log entries to " + rootPath + "/" + dirName);
        System.out.println();

        // 基准时间：2026-03-01 00:00:00 UTC
        long baseEpochMs = 1709164800000L;

        for (int i = 1; i <= numRecords; i++) {
            long entryEpochMs = baseEpochMs + (long) i * 60000L;
            int days = (int) (entryEpochMs / 86400000L);
            long msOfDay = entryEpochMs % 86400000L;

            CmsLogEntry entry = new CmsLogEntry();

            entry.timeOfEntry.msOfDay.value(msOfDay);
            entry.timeOfEntry.daysSince1984.value(days);

            String entryId = String.format("%08d", i);
            entry.entryID.value(entryId.getBytes(StandardCharsets.UTF_8));

            for (String ref : DATASET_REFS) {
                CmsLogDataEntry de = new CmsLogDataEntry();
                de.reference.value(ref);
                de.fc.value(CmsFC.ST);

                CmsData data = new CmsData().choice(CmsData.CHOICE_INT32);
                int offset;
                if ("LD0/LLN0.Mod.stVal".equals(ref)) {
                    offset = 1;
                } else if ("LD0/LLN0.Beh.stVal".equals(ref)) {
                    offset = 2;
                } else {
                    offset = 3;
                }
                data.alt_int32(i * 10 + offset);
                de.value = data;

                de.reason.data_change(true);
                de.reason.quality_change(false);
                de.reason.data_update(false);
                de.reason.integrity(false);
                de.reason.general_interrogation(false);
                de.reason.application_trigger(false);

                entry.entryData.add(de);
            }

            System.out.println("  entry[" + i + "] before append:");
            System.out.println(entry);
            System.out.println("  entry[" + i + "] entryData[0] value:");
            System.out.println(entry.entryData.get(0).value);

            storage.append(LOG_REF, entry);

            LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(entryEpochMs), ZoneId.systemDefault());
            System.out.printf("  [%s] entryId=%s  %d data entries%n", dt.toString().replace("T", " "), entryId,
                    entry.entryData.size());
        }

        System.out.println();
        System.out.println("Done. " + numRecords + " entries written.");
        System.out.println("Files: " + rootPath + "/" + dirName + "/log.dat");
        System.out.println("       " + rootPath + "/" + dirName + "/meta.dat");
    }
}
