package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.data.sequence.block.CmsGoCb;
import com.ysh.jcms.data.sequence.block.CmsLcb;
import com.ysh.jcms.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.data.sequence.block.CmsUrcb;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.scl.state.GoCbCache;
import com.ysh.jcms.utils.scl.state.MsvcbCache;
import com.ysh.jcms.utils.scl.state.RcbStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制块（Report/Log/Goose/MSV）相关的 SCL 操作服务。
 * <p>
 * 收编 GetBrcbValues、GetUrcbValues、GetLcbValues、GetGoCbValues、GetMsvcbValues
 * handler 中的重复 SCL 解析逻辑，统一提供从 SCL 模型构建控制块对象的方法。
 */
public class SclControlBlockService {

    private static final Logger log = LoggerFactory.getLogger(SclControlBlockService.class);

    private SclControlBlockService() {
    }

    // ==================== BRCB（8.7.2） ====================

    /**
     * 按 ref 解析 buffered ReportControl，合并 SCL 默认值与运行时状态。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            引用字符串，格式为 {@code ldInst/lnName.cbName}
     * @return BRCB 对象，若未找到则返回 {@code null}
     */
    public static CmsBrcb resolveBrcb(SclIED ied, String ref) {
        SclReportControl rc = findReportControl(ied, ref, true);
        if (rc == null)
            return null;

        CmsBrcb brcb = new CmsBrcb();
        applySclDefaults(brcb, rc);

        // Overlay runtime state if present
        CmsBrcb runtime = RcbStateManager.get(ref);
        if (runtime != null) {
            applyRuntimeState(brcb, runtime);
        }

        return brcb;
    }

    // ==================== URCB（8.7.4） ====================

    /**
     * 按 ref 解析 unbuffered ReportControl，合并 SCL 默认值与运行时状态。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            引用字符串
     * @return URCB 对象，若未找到则返回 {@code null}
     */
    public static CmsUrcb resolveUrcb(SclIED ied, String ref) {
        SclReportControl rc = findReportControl(ied, ref, false);
        if (rc == null)
            return null;

        CmsUrcb urcb = new CmsUrcb();
        if (rc.rptID() != null)
            urcb.rptID(rc.rptID());
        if (rc.datSet() != null)
            urcb.datSet(rc.datSet());
        if (rc.confRev() != null) {
            try {
                urcb.confRev(Long.parseLong(rc.confRev()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.bufTime() != null) {
            try {
                urcb.bufTm(Long.parseLong(rc.bufTime()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.intgPd() != null) {
            try {
                urcb.intgPd(Long.parseLong(rc.intgPd()));
            } catch (NumberFormatException ignored) {
            }
        }
        urcb.rptEna(false);
        urcb.sqNum(0);
        urcb.gi(false);
        urcb.setPresent("owner", false);

        // Overlay runtime state if present
        CmsBrcb runtime = RcbStateManager.get(ref);
        if (runtime != null) {
            if (runtime.rptID.value() != null && !runtime.rptID.value().isEmpty()) {
                urcb.rptID(runtime.rptID.value());
            }
            urcb.rptEna(runtime.rptEna.value());
            if (runtime.datSet.value() != null && !runtime.datSet.value().isEmpty()) {
                urcb.datSet(runtime.datSet.value());
            }
            if (runtime.optFlds != null) {
                urcb.optFlds(runtime.optFlds);
            }
            if (runtime.bufTm != null) {
                urcb.bufTm(runtime.bufTm.value());
            }
            if (runtime.sqNum != null) {
                urcb.sqNum(runtime.sqNum.value());
            }
            if (runtime.trgOps != null) {
                urcb.trgOps(runtime.trgOps);
            }
            if (runtime.intgPd != null) {
                urcb.intgPd(runtime.intgPd.value());
            }
            urcb.gi(runtime.gi.value());
            if (runtime.isPresent("owner")) {
                if (runtime.owner.value() != null && runtime.owner.value().length > 0) {
                    urcb.owner(runtime.owner.value());
                }
            }
        }

        return urcb;
    }

    // ==================== LCB（8.8.2） ====================

    /**
     * 按 ref 解析 LogControl。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            引用字符串
     * @return LCB 对象，若未找到则返回 {@code null}
     */
    public static CmsLcb resolveLcb(SclIED ied, String ref) {
        if (!SclRefParser.isValid(ref))
            return null;
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null)
            return null;

        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null)
            return null;

        SclLogControl lc = null;
        for (SclLogControl c : ln.logControls()) {
            if (c.name().equals(cbName)) {
                lc = c;
                break;
            }
        }
        if (lc == null)
            return null;

        CmsLcb lcb = new CmsLcb();
        if (lc.logEna() != null)
            lcb.logEna("true".equalsIgnoreCase(lc.logEna()) || "1".equals(lc.logEna()));
        if (lc.datSet() != null)
            lcb.datSet(lc.datSet());
        if (lc.intgPd() != null) {
            try {
                lcb.intgPd(Long.parseLong(lc.intgPd()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (lc.logName() != null)
            lcb.logRef(lc.logName());
        if (lc.optFields() != null) {
            try {
                long v = Long.parseLong(lc.optFields());
                CmsLcbOptFlds f = new CmsLcbOptFlds().bit0(v != 0);
                lcb.optFlds(f);
            } catch (NumberFormatException ignored) {
            }
        }
        if (lc.trgOps() != null) {
            lcb.trgOps(new CmsTriggerConditions().integrity(true));
        }
        return lcb;
    }

    // ==================== GoCB（8.10.2） ====================

    /**
     * 按 ref 解析 GSEControl，优先使用缓存（由 SetGoCBValues 写入）。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            引用字符串
     * @return GoCB 对象，若未找到则返回 {@code null}
     */
    public static CmsGoCb resolveGocb(SclIED ied, String ref) {
        // Check in-memory cache first (written by SetGoCBValues)
        CmsGoCb cached = GoCbCache.get(ref);
        if (cached != null) {
            log.debug("resolveGocb: cache hit for '{}'", ref);
            return cached;
        }

        if (!SclRefParser.isValid(ref)) {
            log.warn("resolveGocb: invalid ref format '{}'", ref);
            return null;
        }
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnPart = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null) {
            log.warn("resolveGocb: invalid ref format '{}' (no CB name)", ref);
            return null;
        }

        SclLDevice device = findLd(ied, ldName);
        if (device == null) {
            log.warn("resolveGocb: LD '{}' not found", ldName);
            return null;
        }

        // Try findLnByFullName first (exact match)
        SclLN ln = device.findLnByFullName(lnPart);
        if (ln != null) {
            SclGSEControl gc = ln.findGseControlByName(cbName);
            if (gc != null)
                return buildGocb(gc);
            log.debug("resolveGocb: GSEControl '{}' not in LN '{}' (exact match)", cbName, ln.getFullName());
        }

        // Fallback: prefix match (e.g. lnPart="CTRL" matches LN "CTRL1")
        for (SclLN candidate : device.lns()) {
            String fullName = candidate.getFullName();
            if (fullName.startsWith(lnPart)) {
                SclGSEControl gc = candidate.findGseControlByName(cbName);
                if (gc != null) {
                    log.debug("resolveGocb: found GSEControl in LN '{}' (prefix match)", fullName);
                    return buildGocb(gc);
                }
            }
        }
        log.warn("resolveGocb: GSEControl '{}' not found in any LN matching '{}' under LD '{}'", cbName, lnPart, ldName);
        return null;
    }

    // ==================== MSVCB（8.11.2） ====================

    /**
     * 按 ref 解析 SampledValueControl，优先使用缓存（由 SetMSVCBValues 写入）。
     *
     * @param ied
     *            IED 模型
     * @param ref
     *            引用字符串
     * @return MSVCB 对象，若未找到则返回 {@code null}
     */
    public static CmsMsvcb resolveMsvcb(SclIED ied, String ref) {
        // Check in-memory cache first (written by SetMSVCBValues)
        CmsMsvcb cached = MsvcbCache.get(ref);
        if (cached != null) {
            log.debug("resolveMsvcb: cache hit for '{}'", ref);
            return cached;
        }

        if (!SclRefParser.isValid(ref)) {
            log.warn("resolveMsvcb: invalid ref format '{}'", ref);
            return null;
        }
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnPart = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null) {
            log.warn("resolveMsvcb: invalid ref format '{}' (no CB name)", ref);
            return null;
        }

        SclLDevice device = findLd(ied, ldName);
        if (device == null) {
            log.warn("resolveMsvcb: LD '{}' not found", ldName);
            return null;
        }

        // Try exact name match first
        SclLN ln = device.findLnByFullName(lnPart);
        if (ln != null) {
            SclSampledValueControl svc = ln.findSmvControlByName(cbName);
            if (svc != null)
                return buildMsvcb(svc);
            log.debug("resolveMsvcb: SampledValueControl '{}' not in LN '{}' (exact match)", cbName, ln.getFullName());
        }

        // Fallback: prefix match (e.g. lnPart="SV" matches LN "SV1")
        for (SclLN candidate : device.lns()) {
            String fullName = candidate.getFullName();
            if (fullName.startsWith(lnPart)) {
                SclSampledValueControl svc = candidate.findSmvControlByName(cbName);
                if (svc != null) {
                    log.debug("resolveMsvcb: found SampledValueControl in LN '{}' (prefix match)", fullName);
                    return buildMsvcb(svc);
                }
            }
        }
        log.warn("resolveMsvcb: SampledValueControl '{}' not found in any LN matching '{}' under LD '{}'", cbName, lnPart, ldName);
        return null;
    }

    // ==================== 内部辅助方法 ====================

    /**
     * 查找 buffered (buffered=true) 或 unbuffered (buffered=false) 的 ReportControl。
     */
    private static SclReportControl findReportControl(SclIED ied, String ref, boolean buffered) {
        if (!SclRefParser.isValid(ref))
            return null;
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null)
            return null;

        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null)
            return null;

        for (SclReportControl c : ln.reportControls()) {
            boolean isBuffered = "true".equals(c.buffered());
            if (isBuffered == buffered && c.name().equals(cbName)) {
                return c;
            }
        }
        return null;
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }

    private static CmsGoCb buildGocb(SclGSEControl gc) {
        CmsGoCb gocb = new CmsGoCb();
        if (gc.appID() != null)
            gocb.goID(gc.appID());
        if (gc.datSet() != null)
            gocb.datSet(gc.datSet());
        if (gc.confRev() != null) {
            try {
                gocb.confRev(Long.parseLong(gc.confRev()));
            } catch (NumberFormatException ignored) {
            }
        }
        return gocb;
    }

    private static CmsMsvcb buildMsvcb(SclSampledValueControl svc) {
        CmsMsvcb msvcb = new CmsMsvcb();
        if (svc.svID() != null)
            msvcb.msvID(svc.svID());
        if (svc.datSet() != null)
            msvcb.datSet(svc.datSet());
        if (svc.confRev() != null) {
            try {
                msvcb.confRev(Long.parseLong(svc.confRev()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (svc.smpRate() != null && !svc.smpRate().isEmpty()) {
            try {
                msvcb.smpRate(Integer.parseInt(svc.smpRate()));
            } catch (NumberFormatException ignored) {
            }
        }
        return msvcb;
    }

    /** Apply SCL template defaults to a fresh CmsBrcb. */
    private static void applySclDefaults(CmsBrcb brcb, SclReportControl rc) {
        if (rc.rptID() != null)
            brcb.rptID(rc.rptID());
        if (rc.datSet() != null)
            brcb.datSet(rc.datSet());
        if (rc.confRev() != null) {
            try {
                brcb.confRev(Long.parseLong(rc.confRev()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.bufTime() != null) {
            try {
                brcb.bufTm(Long.parseLong(rc.bufTime()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.intgPd() != null) {
            try {
                brcb.intgPd(Long.parseLong(rc.intgPd()));
            } catch (NumberFormatException ignored) {
            }
        }
        brcb.rptEna(false);
        brcb.sqNum(0);
        brcb.gi(false);
        brcb.purgeBuf(false);
        brcb.entryID(new byte[8]);
        brcb.setPresent("resvTms", false);
        brcb.setPresent("owner", false);
    }

    /** Overlay runtime-modified BRCB fields onto the base instance. */
    private static void applyRuntimeState(CmsBrcb brcb, CmsBrcb runtime) {
        if (runtime.rptID.value() != null && !runtime.rptID.value().isEmpty())
            brcb.rptID(runtime.rptID.value());
        if (runtime.datSet.value() != null && !runtime.datSet.value().isEmpty())
            brcb.datSet(runtime.datSet.value());
        if (runtime.optFlds != null)
            brcb.optFlds(runtime.optFlds);
        if (runtime.trgOps != null)
            brcb.trgOps(runtime.trgOps);
        if (runtime.timeOfEntry != null)
            brcb.timeOfEntry = runtime.timeOfEntry;
        if (runtime.bufTm != null)
            brcb.bufTm(runtime.bufTm.value());
        if (runtime.sqNum != null)
            brcb.sqNum(runtime.sqNum.value());
        if (runtime.intgPd != null)
            brcb.intgPd(runtime.intgPd.value());
        if (runtime.entryID.value() != null && runtime.entryID.value().length > 0)
            brcb.entryID(runtime.entryID.value());
        brcb.rptEna(runtime.rptEna.value());
        brcb.gi(runtime.gi.value());
        brcb.purgeBuf(runtime.purgeBuf.value());
        if (runtime.isPresent("resvTms")) {
            brcb.resvTms(runtime.resvTms.value());
        }
        if (runtime.isPresent("owner")) {
            if (runtime.owner.value() != null && runtime.owner.value().length > 0)
                brcb.owner(runtime.owner.value());
        }
    }
}
