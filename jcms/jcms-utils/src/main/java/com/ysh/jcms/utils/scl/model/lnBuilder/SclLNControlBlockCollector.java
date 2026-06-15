package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
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

    public static List<SclCBEntry> collectCBValues(SclLNBase ln, int acsiClass, CmsServerSession session) {
        List<SclCBEntry> result = new ArrayList<>();
        switch (acsiClass) {
            case CmsACSIClass.SGCB: {
                CmsConfig.Setting setting = CmsConfigLoader.load().getSetting();
                if (setting.isSgDefaultEnabled()) {
                    String sgName = setting.getSgDefaultName();
                    String entryRef = ln.getFullName() + "." + sgName;
                    String ldName = ln.getParent() != null ? ln.getParent().getInst() : "";
                    String fullRef = ldName + "/" + entryRef;
                    result.add(new SclCBEntry(entryRef, SgcbBuilder.buildSgcb(fullRef, session)));
                }
                break;
            }

            case CmsACSIClass.BRCB:
                for (SclReportControl rc : ln.getReportControls()) {
                    if (rc.isBuffered()) {
                        result.add(new SclCBEntry(rc.getName(), BrcbBuilder.buildBrcb(rc)));
                    }
                }
                break;

            case CmsACSIClass.URCB:
                for (SclReportControl urc : ln.getReportControls()) {
                    if (!urc.isBuffered()) {
                        result.add(new SclCBEntry(urc.getName(), UrcbBuilder.buildUrcb(urc)));
                    }
                }
                break;

            case CmsACSIClass.LCB:
                for (SclLogControl lc : ln.getLogControls()) {
                    result.add(new SclCBEntry(lc.getName(), LcbBuilder.buildLcb(lc)));
                }
                break;

            case CmsACSIClass.GO_CB:
                for (SclGSEControl gc : ln.getGseControls()) {
                    result.add(new SclCBEntry(gc.getName(), GocbBuilder.buildGocb(gc)));
                }
                break;

            case CmsACSIClass.MSV_CB:
                for (SclSampledValueControl svc : ln.getSvControls()) {
                    result.add(new SclCBEntry(svc.getName(), MsvcbBuilder.buildMsvcb(svc)));
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported ACSI class: " + acsiClass);
        }

        return result;
    }
}


