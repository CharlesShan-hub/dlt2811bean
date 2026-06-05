package com.ysh.jcms.service.connection;

import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.ffi.CmsType;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class MiniTest {
    public static class Mini extends CmsType {
        public CmsUint8Array.ByValue data = new CmsUint8Array.ByValue();
        public CmsInt32U.ByValue count = new CmsInt32U.ByValue();
        public Mini() { super(false); }
        @Override
        protected List<String> getFieldOrder() { return Arrays.asList("data", "count"); }
    }

    @Test
    void testWriteReadByValue() {
        Mini m = new Mini();
        m.data.bytes("hello");
        m.count.value(42);
        m.write();

        byte[] raw = m.getPointer().getByteArray(0, m.size());
        System.out.println("Native bytes (" + raw.length + "):");
        for (int i = 0; i < raw.length; i++) {
            System.out.printf("%02x ", raw[i] & 0xff);
            if ((i + 1) % 8 == 0) System.out.println();
        }
        System.out.println();
    }
}
