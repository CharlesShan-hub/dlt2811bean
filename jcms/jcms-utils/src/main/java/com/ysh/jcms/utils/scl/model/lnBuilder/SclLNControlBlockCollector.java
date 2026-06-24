package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.data.SclCBEntry;
import com.ysh.jcms.utils.scl.model.ied.SclLNBase;
import com.ysh.jcms.svc.directory.CmsAcsiClass;

import java.util.ArrayList;
import java.util.List;

public class SclLNControlBlockCollector {

    public static List<SclCBEntry> collectCBValues(SclLNBase ln, int acsiClass) {
        List<SclCBEntry> result = new ArrayList<>();
        switch (acsiClass) {
            case CmsAcsiClass.SGECB: {
                String entryRef = SgcbBuilder.buildEntryRef(ln);
                if (entryRef != null) {
                    result.add(new SclCBEntry(entryRef, SgcbBuilder.defaultSgcb()));
                }
                break;
            }

            case CmsAcsiClass.BRCB:
                for (SclReportControl rc : ln.getReportControls()) {
                    if (rc.isBuffered()) {
                        result.add(new SclCBEntry(rc.getName(), BrcbBuilder.from(rc)));
                    }
                }
                break;

            case CmsAcsiClass.URCB:
                for (SclReportControl urc : ln.getReportControls()) {
                    if (!urc.isBuffered()) {
                        result.add(new SclCBEntry(urc.getName(), UrcbBuilder.from(urc)));
                    }
                }
                break;

            case CmsAcsiClass.LCB:
                for (SclLogControl lc : ln.getLogControls()) {
                    result.add(new SclCBEntry(lc.getName(), LcbBuilder.from(lc)));
                }
                break;

            case CmsAcsiClass.GOCB:
                for (SclGSEControl gc : ln.getGseControls()) {
                    result.add(new SclCBEntry(gc.getName(), GocbBuilder.from(gc)));
                }
                break;

            case CmsAcsiClass.MSVCB:
                for (SclSampledValueControl svc : ln.getSvControls()) {
                    result.add(new SclCBEntry(svc.getName(), MsvcbBuilder.from(svc)));
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported ACSI class: " + acsiClass);
        }

        return result;
    }
}
