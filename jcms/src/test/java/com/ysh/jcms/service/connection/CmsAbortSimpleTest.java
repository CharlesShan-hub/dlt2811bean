package com.ysh.jcms.service.connection;

import org.junit.jupiter.api.Test;

class CmsAbortSimpleTest {
    @Test
    void test() {
        CmsAbort a = new CmsAbort();
        System.out.println("Size: " + a.size());
        System.out.println("AssocId size: " + new com.ysh.jcms.service.other.CmsAssociationId.ByValue().size());
        System.out.println("Reason size: " + new CmsAbortReason.ByValue().size());
    }
}
