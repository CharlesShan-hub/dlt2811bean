package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.core.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.core.data.sequence.block.CmsUrcb;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.scl.state.CbStateManager;
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
     * <p>
     * 在指定 AP 作用域内查找。
     *
     * @param ied
     *            IED 模型
     * @param ap
     *            当前关联的访问点
     * @param ref
     *            引用字符串，格式为 {@code ldInst/lnName.cbName}
     * @return BRCB 对象，若未找到则返回 {@code null}
     */
    public static CmsBrcb resolveBrcb(SclIED ied, SclAccessPoint ap, String ref) {
        return buildBrcb(findReportControl(ied, ap, ref, true), ref);
    }

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
        return buildBrcb(findReportControl(ied, null, ref, true), ref);
    }

    private static CmsBrcb buildBrcb(SclReportControl rc, String ref) {
        if (rc == null)
            return null;

        CmsBrcb brcb = new CmsBrcb();
        applySclDefaults(brcb, rc);

        // Overlay runtime state if present
        CmsBrcb runtime = CbStateManager.RCB.get(ref);
        if (runtime != null) {
            applyRuntimeState(brcb, runtime);
        }

        return brcb;
    }

    // ==================== URCB（8.7.4） ====================

    /**
     * 按 ref 解析 unbuffered ReportControl，合并 SCL 默认值与运行时状态。
     * <p>
     * 在指定 AP 作用域内查找。
     *
     * @param ied
     *            IED 模型
     * @param ap
     *            当前关联的访问点
     * @param ref
     *            引用字符串
     * @return URCB 对象，若未找到则返回 {@code null}
     */
    public static CmsUrcb resolveUrcb(SclIED ied, SclAccessPoint ap, String ref) {
        return buildUrcb(findReportControl(ied, ap, ref, false), ref);
    }

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
        return buildUrcb(findReportControl(ied, null, ref, false), ref);
    }

    private static CmsUrcb buildUrcb(SclReportControl rc, String ref) {
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
            } catch (NumberFormatException e) {
                log.warn("SCL confRev '{}' 非数字，URCB 回退默认值", rc.confRev(), e);
            }
        }
        if (rc.bufTime() != null) {
            try {
                urcb.bufTm(Long.parseLong(rc.bufTime()));
            } catch (NumberFormatException e) {
                log.warn("SCL bufTime '{}' 非数字，URCB 回退默认值", rc.bufTime(), e);
            }
        }
        if (rc.intgPd() != null) {
            try {
                urcb.intgPd(Long.parseLong(rc.intgPd()));
            } catch (NumberFormatException e) {
                log.warn("SCL intgPd '{}' 非数字，URCB 回退默认值", rc.intgPd(), e);
            }
        }
        urcb.rptEna(false);
        urcb.sqNum(0);
        urcb.gi(false);
        urcb.setPresent("owner", false);

        // Overlay runtime state if present
        CmsBrcb runtime = CbStateManager.RCB.get(ref);
        if (runtime != null) {
            overlayUrcbRuntime(urcb, runtime);
        }

        return urcb;
    }

    /**
     * Overlay URCB runtime state (carried by CmsBrcb) onto an SCL-built CmsUrcb.
     */
    public static void overlayUrcbRuntime(CmsUrcb urcb, CmsBrcb runtime) {
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
        return resolveLcb(ied, null, ref);
    }

    /**
     * 按 ref 解析 LogControl（AP 作用域）。
     *
     * @param ied
     *            IED 模型
     * @param ap
     *            当前关联的访问点；为 {@code null} 时在全 IED 范围内查找
     * @param ref
     *            引用字符串
     * @return LCB 对象，若未找到则返回 {@code null}
     */
    public static CmsLcb resolveLcb(SclIED ied, SclAccessPoint ap, String ref) {
        if (!SclRefParser.isValid(ref))
            return null;
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null)
            return null;

        SclLN ln = (ap != null) ? findLn(ap, ldName, lnName) : findLn(ied, ldName, lnName);
        if (ln == null)
            return null;

        return buildLcb(ln, cbName);
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
        return resolveGocb(ied, null, ref);
    }

    /**
     * 按 ref 解析 GSEControl，优先使用缓存（由 SetGoCBValues 写入）。
     * <p>
     * 在指定 AP 作用域内查找。
     *
     * @param ied
     *            IED 模型
     * @param ap
     *            当前关联的访问点；为 {@code null} 时在全 IED 范围内查找
     * @param ref
     *            引用字符串
     * @return GoCB 对象，若未找到则返回 {@code null}
     */
    public static CmsGoCb resolveGocb(SclIED ied, SclAccessPoint ap, String ref) {
        // Check in-memory cache first (written by SetGoCBValues)
        CmsGoCb cached = CbStateManager.GOCB.get(ref);
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

        SclLDevice device = (ap != null) ? findLd(ap, ldName) : findLd(ied, ldName);
        if (device == null) {
            log.warn("resolveGocb: LD '{}' not found{}", ldName, ap != null ? " in AP scope" : "");
            return null;
        }

        return findGocbInDevice(device, lnPart, cbName, ref);
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
        return resolveMsvcb(ied, null, ref);
    }

    /**
     * 按 ref 解析 SampledValueControl，优先使用缓存（由 SetMSVCBValues 写入）。
     * <p>
     * 在指定 AP 作用域内查找。
     *
     * @param ied
     *            IED 模型
     * @param ap
     *            当前关联的访问点；为 {@code null} 时在全 IED 范围内查找
     * @param ref
     *            引用字符串
     * @return MSVCB 对象，若未找到则返回 {@code null}
     */
    public static CmsMsvcb resolveMsvcb(SclIED ied, SclAccessPoint ap, String ref) {
        // Check in-memory cache first (written by SetMSVCBValues)
        CmsMsvcb cached = CbStateManager.MSVCB.get(ref);
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

        SclLDevice device = (ap != null) ? findLd(ap, ldName) : findLd(ied, ldName);
        if (device == null) {
            log.warn("resolveMsvcb: LD '{}' not found{}", ldName, ap != null ? " in AP scope" : "");
            return null;
        }

        return findMsvcbInDevice(device, lnPart, cbName, ref);
    }

    // ==================== 内部辅助方法 ====================

    /**
     * 查找 buffered (buffered=true) 或 unbuffered (buffered=false) 的 ReportControl。
     * <p>
     * 在指定 AP 作用域内查找；ap 为 {@code null} 时在全 IED 范围内查找。
     */
    private static SclReportControl findReportControl(SclIED ied, SclAccessPoint ap, String ref, boolean buffered) {
        if (!SclRefParser.isValid(ref))
            return null;
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null)
            return null;

        SclLN ln = (ap != null) ? findLn(ap, ldName, lnName) : findLn(ied, ldName, lnName);
        if (ln == null)
            return null;

        return findReportControlInLn(ln, cbName, buffered);
    }

    private static SclReportControl findReportControlInLn(SclLN ln, String cbName, boolean buffered) {
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

    /** AP 作用域查找 LN。 */
    private static SclLN findLn(SclAccessPoint ap, String ldName, String lnName) {
        SclLDevice ld = Navigator.findLd(ap, ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }

    /** AP 作用域查找 LD。 */
    private static SclLDevice findLd(SclAccessPoint ap, String ldName) {
        return Navigator.findLd(ap, ldName);
    }

    /** 在指定 LD 设备中查找 GSEControl（支持精确匹配和前缀匹配）。 */
    private static CmsGoCb findGocbInDevice(SclLDevice device, String lnPart, String cbName, String ref) {
        // Try findLnByFullName first (exact match)
        SclLN ln = device.findLnByFullName(lnPart);
        if (ln != null) {
            SclGSEControl gc = ln.findGseControlByName(cbName);
            if (gc != null)
                return buildGocb(gc);
            log.debug("findGocbInDevice: GSEControl '{}' not in LN '{}' (exact match)", cbName, ln.getFullName());
        }

        // Fallback: prefix match (e.g. lnPart="CTRL" matches LN "CTRL1")
        for (SclLN candidate : device.lns()) {
            String fullName = candidate.getFullName();
            if (fullName.startsWith(lnPart)) {
                SclGSEControl gc = candidate.findGseControlByName(cbName);
                if (gc != null) {
                    log.debug("findGocbInDevice: found GSEControl in LN '{}' (prefix match)", fullName);
                    return buildGocb(gc);
                }
            }
        }
        log.warn("findGocbInDevice: GSEControl '{}' not found in any LN matching '{}' under LD '{}'", cbName, lnPart, device.inst());
        return null;
    }

    /** 在指定 LD 设备中查找 SampledValueControl（支持精确匹配和前缀匹配）。 */
    private static CmsMsvcb findMsvcbInDevice(SclLDevice device, String lnPart, String cbName, String ref) {
        // Try exact name match first
        SclLN ln = device.findLnByFullName(lnPart);
        if (ln != null) {
            SclSampledValueControl svc = ln.findSmvControlByName(cbName);
            if (svc != null)
                return buildMsvcb(svc);
            log.debug("findMsvcbInDevice: SampledValueControl '{}' not in LN '{}' (exact match)", cbName, ln.getFullName());
        }

        // Fallback: prefix match (e.g. lnPart="SV" matches LN "SV1")
        for (SclLN candidate : device.lns()) {
            String fullName = candidate.getFullName();
            if (fullName.startsWith(lnPart)) {
                SclSampledValueControl svc = candidate.findSmvControlByName(cbName);
                if (svc != null) {
                    log.debug("findMsvcbInDevice: found SampledValueControl in LN '{}' (prefix match)", fullName);
                    return buildMsvcb(svc);
                }
            }
        }
        log.warn("findMsvcbInDevice: SampledValueControl '{}' not found in any LN matching '{}' under LD '{}'", cbName, lnPart,
                device.inst());
        return null;
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
            } catch (NumberFormatException e) {
                log.warn("SCL confRev '{}' 非数字，GoCB 回退默认值", gc.confRev(), e);
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
            } catch (NumberFormatException e) {
                log.warn("SCL confRev '{}' 非数字，MSVCB 回退默认值", svc.confRev(), e);
            }
        }
        if (svc.smpRate() != null && !svc.smpRate().isEmpty()) {
            try {
                msvcb.smpRate(Integer.parseInt(svc.smpRate()));
            } catch (NumberFormatException e) {
                log.warn("SCL smpRate '{}' 非数字，MSVCB 回退默认值", svc.smpRate(), e);
            }
        }
        return msvcb;
    }

    private static CmsLcb buildLcb(SclLN ln, String cbName) {
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
            } catch (NumberFormatException e) {
                log.warn("SCL intgPd '{}' 非数字，LCB 回退默认值", lc.intgPd(), e);
            }
        }
        if (lc.logName() != null)
            lcb.logRef(lc.logName());
        if (lc.optFields() != null) {
            try {
                long v = Long.parseLong(lc.optFields());
                CmsLcbOptFlds f = new CmsLcbOptFlds().bit0(v != 0);
                lcb.optFlds(f);
            } catch (NumberFormatException e) {
                log.warn("SCL optFields '{}' 非数字，LCB 回退默认值", lc.optFields(), e);
            }
        }
        if (lc.trgOps() != null) {
            lcb.trgOps(new CmsTriggerConditions().integrity(true));
        }
        return lcb;
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
            } catch (NumberFormatException e) {
                log.warn("SCL confRev '{}' 非数字，BRCB 回退默认值", rc.confRev(), e);
            }
        }
        if (rc.bufTime() != null) {
            try {
                brcb.bufTm(Long.parseLong(rc.bufTime()));
            } catch (NumberFormatException e) {
                log.warn("SCL bufTime '{}' 非数字，BRCB 回退默认值", rc.bufTime(), e);
            }
        }
        if (rc.intgPd() != null) {
            try {
                brcb.intgPd(Long.parseLong(rc.intgPd()));
            } catch (NumberFormatException e) {
                log.warn("SCL intgPd '{}' 非数字，BRCB 回退默认值", rc.intgPd(), e);
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
    public static void applyRuntimeState(CmsBrcb brcb, CmsBrcb runtime) {
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
