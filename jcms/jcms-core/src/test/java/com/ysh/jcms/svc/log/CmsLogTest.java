package com.ysh.jcms.svc.log;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsLogTest {

    @Test
    public void query_log_by_time_response_with_multiple_entries() {
        /* 模拟真实 log 场景：
         * CmsQueryLogByTimeResponse
         *   ├─ reqId
         *   ├─ logEntry (CmsArray<CmsLogEntry>) — 10 条 entry
         *   │   └─ 每条 CmsLogEntry 有:
         *   │       ├─ timeOfEntry
         *   │       ├─ entryId
         *   │       └─ entryData (CmsArray<CmsLogDataEntry>) — 3 条 data entry
         *   │           └─ 每条 CmsLogDataEntry:
         *   │               ├─ reference
         *   │               ├─ fc
         *   │               ├─ value (CmsData) — CHOICE_INT32
         *   │               └─ reason
         *   └─ moreFollows
         */
        CmsQueryLogByTimeResponse a = new CmsQueryLogByTimeResponse();
        a.reqId.value(1);

        for (int i = 1; i <= 10; i++) {
            CmsLogEntry entry = new CmsLogEntry();
            entry.timeOfEntry.msOfDay.value((long)i * 60000);
            entry.timeOfEntry.daysSince1984.value(5000 + i);
            entry.entryId.value(String.format("%06d", i).getBytes());

            for (int j = 0; j < 3; j++) {
                CmsLogDataEntry de = new CmsLogDataEntry();
                de.reference.value(("ref" + i + "_" + j).getBytes());
                de.fc.value(com.ysh.jcms.data.fc.CmsFC.ST);
                de.value.choice(CmsData.CHOICE_INT32);
                de.value.alt_int32.value(i * 10 + j);
                de.reason.data_change.value(true);
                de.reason.quality_change.value(true);
                entry.entryData.add(de);
            }

            a.logEntry.add(entry);
        }
        a.moreFollows.value(false);

        byte[] encoded = a.encode();
        System.out.println("response: encoded " + encoded.length + " bytes");

        CmsQueryLogByTimeResponse b = new CmsQueryLogByTimeResponse();
        b.decode(encoded);

        System.out.println("b.logEntry.items.size = " + b.logEntry.items.size() + " (expected 10)");
        assertEquals(10, b.logEntry.items.size());
        assertEquals(1, b.reqId.value());
        assertEquals(false, b.moreFollows.value());

        for (int i = 0; i < 10; i++) {
            CmsLogEntry be = b.logEntry.items.get(i);
            assertEquals(3, be.entryData.items.size());
            for (int j = 0; j < 3; j++) {
                CmsLogDataEntry bde = be.entryData.items.get(j);
                assertEquals(CmsData.CHOICE_INT32, bde.value.choice.value());
                assertEquals((i+1) * 10 + j, bde.value.alt_int32.value());
            }
        }
    }

    @Test
    public void mock_generator_identical_data() {
        /* 完全复制 MockLogGenerator 的数据生成逻辑
         * - 3 条 dataset ref
         * - 10 条 entry
         * - value = CHOICE_INT32, 值 = i*10 + offset
         * - 基准时间 1709164800000L (2026-03-01 00:00:00 UTC)
         * - reason: data_change=true, 其他 false
         */
        String[] DATASET_REFS = {
            "LD0/LLN0.Mod.stVal",
            "LD0/LLN0.Beh.stVal",
            "LD0/LLN0.Health.stVal",
        };
        int numRecords = 10;
        long baseEpochMs = 1709164800000L;

        CmsQueryLogByTimeResponse a = new CmsQueryLogByTimeResponse();
        a.reqId.value(123);

        for (int i = 1; i <= numRecords; i++) {
            long entryEpochMs = baseEpochMs + (long) i * 60000L;
            int  days    = (int) (entryEpochMs / 86400000L);
            long msOfDay = entryEpochMs % 86400000L;

            CmsLogEntry entry = new CmsLogEntry();
            entry.timeOfEntry.msOfDay.value(msOfDay);
            entry.timeOfEntry.daysSince1984.value(days);

            byte[] entryIdBytes = String.format("%08d", i).getBytes(java.nio.charset.StandardCharsets.UTF_8);
            entry.entryId.value(entryIdBytes);

            for (int ri = 0; ri < DATASET_REFS.length; ri++) {
                CmsLogDataEntry de = new CmsLogDataEntry();
                de.reference.value(DATASET_REFS[ri].getBytes(java.nio.charset.StandardCharsets.UTF_8));
                de.fc.value(com.ysh.jcms.data.fc.CmsFC.ST);

                int offset = ri + 1; // 1, 2, 3
                de.value.choice(CmsData.CHOICE_INT32);
                de.value.alt_int32.value(i * 10 + offset);

                de.reason.data_change.value(true);
                de.reason.quality_change.value(false);
                de.reason.data_update.value(false);
                de.reason.integrity.value(false);
                de.reason.general_interrogation.value(false);
                de.reason.application_trigger.value(false);

                entry.entryData.add(de);
            }
            a.logEntry.add(entry);
        }
        a.moreFollows.value(false);

        byte[] encoded = a.encode();
        // System.out.println("mock_generator test: encoded " + encoded.length + " bytes");

        CmsQueryLogByTimeResponse b = new CmsQueryLogByTimeResponse();
        b.decode(encoded);

        // System.out.println(b.toString());

        assertEquals(10, b.logEntry.items.size());
        assertEquals(123, b.reqId.value());
        assertEquals(false, b.moreFollows.value());

        for (int i = 0; i < 10; i++) {
            CmsLogEntry be = b.logEntry.items.get(i);
            assertEquals(3, be.entryData.items.size());

            long expectedEpochMs = baseEpochMs + (long)(i+1) * 60000L;
            int  expectedDays = (int)(expectedEpochMs / 86400000L);
            long expectedMsOfDay = expectedEpochMs % 86400000L;
            assertEquals(expectedMsOfDay, be.timeOfEntry.msOfDay.value());
            assertEquals(expectedDays, be.timeOfEntry.daysSince1984.value());

            byte[] expectedEntryId = String.format("%08d", i+1).getBytes(java.nio.charset.StandardCharsets.UTF_8);
            assertArrayEquals(expectedEntryId, be.entryId.value());

            for (int j = 0; j < 3; j++) {
                CmsLogDataEntry bde = be.entryData.items.get(j);
                assertEquals(CmsData.CHOICE_INT32, bde.value.choice.value());
                assertEquals((i+1) * 10 + (j+1), bde.value.alt_int32.value());
                assertEquals(true, bde.reason.data_change.value());
            }
        }
    }

    @Test
    public void query_log_by_time_request() {
        CmsQueryLogByTimeRequest a = new CmsQueryLogByTimeRequest();
        a.reqId.value(30);
        a.logReference.value("logRef".getBytes());
        a.startTimePresent.value(false);
        a.stopTimePresent.value(false);
        a.entryAfterPresent.value(false);
        byte[] encoded = a.encode();

        CmsQueryLogByTimeRequest b = new CmsQueryLogByTimeRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void query_log_after_request() {
        CmsQueryLogAfterRequest a = new CmsQueryLogAfterRequest();
        a.reqId.value(40);
        a.logReference.value("logRef".getBytes());
        a.startTimePresent.value(false);
        a.entry.value(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        byte[] encoded = a.encode();

        CmsQueryLogAfterRequest b = new CmsQueryLogAfterRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void simple_error() {
        CmsGetLcbValuesError a = new CmsGetLcbValuesError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsGetLcbValuesError b = new CmsGetLcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
