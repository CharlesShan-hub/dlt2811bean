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
