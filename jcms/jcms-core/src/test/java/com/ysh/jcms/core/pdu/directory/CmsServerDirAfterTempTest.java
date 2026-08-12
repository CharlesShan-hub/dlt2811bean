package com.ysh.jcms.core.pdu.directory;

import org.junit.Test;

public class CmsServerDirAfterTempTest {

    @Test
    public void roundtrip_referenceAfter() {
        CmsGetServerDirectoryRequest req = new CmsGetServerDirectoryRequest().objectClass(1).referenceAfter("SWI");
        byte[] data = req.encode();
        System.out.println("encoded len=" + data.length);

        CmsGetServerDirectoryRequest r2 = new CmsGetServerDirectoryRequest();
        r2.decode(data);
        System.out.println("objectClass=" + r2.getObjectClass());
        System.out.println("isPresent(referenceAfter)=" + r2.isPresent("referenceAfter"));
        System.out.println("referenceAfter.value()=" + r2.referenceAfter.value());
    }

    @Test
    public void after_filter_logic() {
        java.util.List<String> items = new java.util.ArrayList<>();
        items.add("PIGO");
        items.add("SWI");
        // mimic BaseServerHandler.after()
        String refAfter = "SWI";
        int idx = items.indexOf(refAfter);
        System.out.println("idx=" + idx);
        java.util.List<String> sub = items.subList(idx + 1, items.size());
        System.out.println("after SWI -> " + sub);
    }
}
