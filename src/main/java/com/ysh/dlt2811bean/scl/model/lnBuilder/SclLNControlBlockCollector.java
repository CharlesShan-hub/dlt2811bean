package com.ysh.dlt2811bean.scl.model.lnBuilder;

import com.ysh.dlt2811bean.scl.model.control.SclGSEControl;
import com.ysh.dlt2811bean.scl.model.control.SclLogControl;
import com.ysh.dlt2811bean.scl.model.control.SclReportControl;
import com.ysh.dlt2811bean.scl.model.control.SclSampledValueControl;
import com.ysh.dlt2811bean.scl.model.data.SclCBEntry;
import com.ysh.dlt2811bean.scl.model.ied.SclLNBase;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

import java.util.ArrayList;
import java.util.List;

public class SclLNControlBlockCollector {

    public static List<SclCBEntry> collectCBValues(SclLNBase ln, int acsiClass, CmsServerSession session) {
        List<SclCBEntry> result = new ArrayList<>();
        if (acsiClass == CmsACSIClass.SGCB) {
            String sgcbRef = ln.getFullName() + ".SG1";
            result.add(new SclCBEntry(sgcbRef, SgcbBuilder.buildSgcb(sgcbRef, session)));
        } else if (acsiClass == CmsACSIClass.BRCB) {
            for (SclReportControl rc : ln.getReportControls()) {
                if (rc.isBuffered()) {
                    result.add(new SclCBEntry(rc.getName(), BrcbBuilder.buildBrcb(rc)));
                }
            }
        } else if (acsiClass == CmsACSIClass.URCB) {
            for (SclReportControl rc : ln.getReportControls()) {
                if (!rc.isBuffered()) {
                    result.add(new SclCBEntry(rc.getName(), UrcbBuilder.buildUrcb(rc)));
                }
            }
        } else if (acsiClass == CmsACSIClass.LCB) {
            for (SclLogControl lc : ln.getLogControls()) {
                result.add(new SclCBEntry(lc.getName(), LcbBuilder.buildLcb(lc)));
            }
        } else if (acsiClass == CmsACSIClass.GO_CB) {
            for (SclGSEControl gc : ln.getGseControls()) {
                result.add(new SclCBEntry(gc.getName(), GocbBuilder.buildGocb(gc)));
            }
        } else if (acsiClass == CmsACSIClass.MSV_CB) {
            for (SclSampledValueControl svc : ln.getSvControls()) {
                result.add(new SclCBEntry(svc.getName(), MsvcbBuilder.buildMsvcb(svc)));
            }
        }
        return result;
    }
}
