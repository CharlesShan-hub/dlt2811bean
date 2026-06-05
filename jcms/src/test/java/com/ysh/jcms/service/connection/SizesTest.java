package com.ysh.jcms.service.connection;

import com.ysh.jcms.datatype.block.CmsBrcb;
import com.ysh.jcms.datatype.block.CmsGoCB;
import com.ysh.jcms.datatype.block.CmsLcb;
import com.ysh.jcms.datatype.basic.*;
import com.ysh.jcms.datatype.common.*;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import com.ysh.jcms.datatype.block.CmsRcbOptFlds;
import com.ysh.jcms.datatype.block.CmsTriggerConditions;
import com.ysh.jcms.service.other.CmsAssociationId;
import org.junit.jupiter.api.Test;

class SizesTest {
    @Test
    void printSizes() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Type sizes (bytes) ===\n");
        sb.append("CmsBoolean.ByValue: " + new CmsBoolean.ByValue().size() + "\n");
        sb.append("CmsInt16.ByValue: " + new CmsInt16.ByValue().size() + "\n");
        sb.append("CmsInt16U.ByValue: " + new CmsInt16U.ByValue().size() + "\n");
        sb.append("CmsInt32U.ByValue: " + new CmsInt32U.ByValue().size() + "\n");
        sb.append("CmsInt64.ByValue: " + new CmsInt64.ByValue().size() + "\n");
        sb.append("CmsUint8Array.ByValue: " + new CmsUint8Array.ByValue().size() + "\n");
        sb.append("CmsObjectReference.ByValue: " + new CmsObjectReference.ByValue().size() + "\n");
        sb.append("CmsEntryId.ByValue: " + new CmsEntryId.ByValue().size() + "\n");
        sb.append("CmsAssociationId.ByValue: " + new CmsAssociationId.ByValue().size() + "\n");
        sb.append("CmsBinaryTime.ByValue: " + new CmsBinaryTime.ByValue().size() + "\n");
        sb.append("CmsRcbOptFlds: " + new CmsRcbOptFlds().size() + "\n");
        sb.append("CmsTriggerConditions: " + new CmsTriggerConditions().size() + "\n");
        sb.append("CmsAbort: " + new CmsAbort().size() + "\n");
        sb.append("CmsAbortReason.ByValue: " + new CmsAbortReason.ByValue().size() + "\n");
        sb.append("CmsBrcb: " + new CmsBrcb().size() + "\n");
        sb.append("CmsGoCB: " + new CmsGoCB().size() + "\n");
        sb.append("CmsLcb: " + new CmsLcb().size() + "\n");
        System.out.print(sb.toString());
    }
}
