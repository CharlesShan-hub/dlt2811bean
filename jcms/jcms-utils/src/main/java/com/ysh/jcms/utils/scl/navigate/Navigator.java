package com.ysh.jcms.utils.scl.navigate;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.*;
import com.ysh.jcms.utils.scl.model.instance.*;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 引用导航器 —— SclRef → 模型元素。
 * <p>
 * 核心入口：{@link #go(SclDocument, SclRef)}。 所有查找操作最终都通过 SclRef 定位到模型元素。
 */
public class Navigator {

    private final SclDocument document;
    private final SclIED ied;
    private final SclLDevice ld;
    private final SclLN ln;
    private final SclDOI doi;
    private final SclSDI sdi;
    private final SclDAI dai;
    private final SclRef ref;

    private Navigator(SclDocument document, SclIED ied, SclLDevice ld, SclLN ln, SclDOI doi, SclSDI sdi, SclDAI dai, SclRef ref) {
        this.document = document;
        this.ied = ied;
        this.ld = ld;
        this.ln = ln;
        this.doi = doi;
        this.sdi = sdi;
        this.dai = dai;
        this.ref = ref;
    }

    // ==================== 核心入口 ====================

    /** 核心方法：按 SclRef 导航到模型元素 */
    public static Navigator go(SclDocument document, SclRef ref) {
        if (document == null || ref == null)
            return empty();

        // 找 IED
        String iedName = ref.iedName();
        if (iedName == null)
            return empty(); // SclRef 必须含 IED 名
        SclIED ied = document.ied(iedName);
        if (ied == null)
            return empty();

        return navigate(ied, ref, document);
    }

    /** 按引用字符串导航（便捷方法） */
    public static Navigator go(SclDocument document, String ref) {
        if (document == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return go(document, SclRefParser.parse(ref));
    }

    /** 在指定 IED 内按 SclRef 导航 */
    public static Navigator go(SclIED ied, SclRef ref) {
        if (ied == null || ref == null)
            return empty();
        return navigate(ied, ref, null);
    }

    /** 在指定 IED 内按字符串导航 */
    public static Navigator go(SclIED ied, String ref) {
        if (ied == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ied, SclRefParser.parse(ref), null);
    }

    /** 在文档 + 指定 IED 内按字符串导航（带 dataTypeTemplates 访问能力） */
    public static Navigator go(SclDocument doc, SclIED ied, String ref) {
        if (doc == null || ied == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ied, SclRefParser.parse(ref), doc);
    }

    /** AP 作用域：在文档 + 指定 AP 内按字符串导航 */
    public static Navigator go(SclDocument doc, SclAccessPoint ap, String ref) {
        if (doc == null || ap == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ap, SclRefParser.parse(ref), doc);
    }

    /** AP 作用域：在指定 IED + AP 内按字符串导航 */
    public static Navigator go(SclIED ied, SclAccessPoint ap, String ref) {
        if (ied == null || ap == null || ref == null || !SclRefParser.isValid(ref))
            return empty();
        return navigate(ap, SclRefParser.parse(ref), null);
    }

    // ==================== 导航逻辑 ====================

    private static Navigator navigate(SclIED ied, SclRef sclRef, SclDocument doc) {
        // 找 LDevice
        SclLDevice ld = findLd(ied, sclRef.ldInst());
        if (ld == null)
            return empty();

        // 找 LN
        SclLN ln = findLn(ld, sclRef.lnName());
        if (ln == null)
            return empty();

        // LN 级别
        if (sclRef.isLnLevel()) {
            return new Navigator(doc, ied, ld, ln, null, null, null, sclRef);
        }

        // DO 级别及以上：找 DOI
        SclDOI doi = ln.findDoiByName(sclRef.doName());
        if (doi == null) {
            // DO 可能在模板中定义但不在实例中（如 Beh）→ 返回无 DOI 的 Navigator，让下游走模板查找
            return new Navigator(doc, ied, ld, ln, null, null, null, sclRef);
        }

        if (sclRef.isDoLevel()) {
            return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        }

        // DA 级别：走 SDI 链→DAI
        SclSDI currentSdi = null;
        boolean sdiFound = true;
        for (String sdiName : sclRef.sdiChain()) {
            SclSDI next = (currentSdi == null) ? doi.findSdiByName(sdiName) : currentSdi.findSdiByName(sdiName);
            if (next == null) {
                sdiFound = false;
                break;
            }
            currentSdi = next;
        }

        if (!sdiFound) {
            // SDI 未在实例中找到 → 可能是 SDO 级引用（模板级 SDO，无实例 SDI）
            // 返回部分 Navigator（有 DOI，无 SDI/DAI），让下游走模板查找
            return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        }

        SclDAI dai = (currentSdi != null) ? currentSdi.findDaiByName(sclRef.daName()) : doi.findDaiByName(sclRef.daName());
        if (dai == null) {
            // DAI 未在实例中找到 → 返回部分 Navigator（有 DOI，无 DAI），让下游走模板查找
            return new Navigator(doc, ied, ld, ln, doi, currentSdi, null, sclRef);
        }

        return new Navigator(doc, ied, ld, ln, doi, currentSdi, dai, sclRef);
    }

    /** AP 作用域导航：只在指定 AP 下查找 LD。 */
    private static Navigator navigate(SclAccessPoint ap, SclRef sclRef, SclDocument doc) {
        SclLDevice ld = findLd(ap, sclRef.ldInst());
        if (ld == null)
            return empty();

        SclLN ln = findLn(ld, sclRef.lnName());
        if (ln == null)
            return empty();

        if (sclRef.isLnLevel()) {
            return new Navigator(doc, null, ld, ln, null, null, null, sclRef);
        }

        SclDOI doi = ln.findDoiByName(sclRef.doName());
        if (doi == null) {
            // DO 可能在模板中定义但不在实例中（如 Beh）→ 返回无 DOI 的 Navigator，让下游走模板查找
            return new Navigator(doc, null, ld, ln, null, null, null, sclRef);
        }

        if (sclRef.isDoLevel()) {
            return new Navigator(doc, null, ld, ln, doi, null, null, sclRef);
        }

        SclSDI currentSdi = null;
        boolean sdiFound = true;
        for (String sdiName : sclRef.sdiChain()) {
            SclSDI next = (currentSdi == null) ? doi.findSdiByName(sdiName) : currentSdi.findSdiByName(sdiName);
            if (next == null) {
                sdiFound = false;
                break;
            }
            currentSdi = next;
        }

        if (!sdiFound) {
            return new Navigator(doc, null, ld, ln, doi, null, null, sclRef);
        }

        SclDAI dai = (currentSdi != null) ? currentSdi.findDaiByName(sclRef.daName()) : doi.findDaiByName(sclRef.daName());
        if (dai == null) {
            return new Navigator(doc, null, ld, ln, doi, currentSdi, null, sclRef);
        }

        return new Navigator(doc, null, ld, ln, doi, currentSdi, dai, sclRef);
    }

    private static SclLDevice findLd(SclIED ied, String ldInst) {
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer server = ap.server();
            if (server != null) {
                SclLDevice ld = server.findLDeviceByInst(ldInst);
                if (ld != null)
                    return ld;
            }
        }
        return null;
    }

    /** AP 作用域查找：只在指定 AP 下找 LD。 */
    public static SclLDevice findLd(SclAccessPoint ap, String ldInst) {
        SclServer server = ap.server();
        if (server != null) {
            return server.findLDeviceByInst(ldInst);
        }
        return null;
    }

    private static SclLN findLn(SclLDevice ld, String lnName) {
        for (SclLN ln : ld.lns()) {
            if (ln.getFullName().equals(lnName))
                return ln;
        }
        return null;
    }

    /**
     * 按 LD 名称或 LN 引用解析逻辑节点列表（AP 作用域）。
     *
     * @param ied
     *            IED 对象
     * @param ap
     *            当前关联的访问点（非 null 时限定在该 AP 下查找）
     * @param ldName
     *            LD 名称（非空时返回该 LD 下所有 LN）
     * @param lnReference
     *            LN 引用（LD/LN 格式，ldName 为空时使用）
     * @return LN 列表，未找到时返回 null
     */
    public static List<SclLN> resolveLns(SclIED ied, SclAccessPoint ap, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = findLd(ap, ldName);
            if (device != null) {
                result.addAll(device.lns());
                return result;
            }
            return null;
        }
        if (lnReference == null || lnReference.isEmpty() || !SclRefParser.isValid(lnReference))
            return null;
        SclRef sclRef = SclRefParser.parse(lnReference);
        SclLDevice device = findLd(ap, sclRef.ldInst());
        if (device != null) {
            SclLN ln = findLn(device, sclRef.lnName());
            if (ln != null) {
                result.add(ln);
                return result;
            }
        }
        return null;
    }

    /**
     * 按 LD 名称或 LN 引用解析逻辑节点列表（跨所有 AP）。
     *
     * @param ied
     *            IED 对象
     * @param ldName
     *            LD 名称（非空时返回该 LD 下所有 LN）
     * @param lnReference
     *            LN 引用（LD/LN 格式，ldName 为空时使用）
     * @return LN 列表，未找到时返回 null
     */
    public static List<SclLN> resolveLns(SclIED ied, String ldName, String lnReference) {
        List<SclLN> result = new ArrayList<>();
        if (ldName != null && !ldName.isEmpty()) {
            SclLDevice device = findLd(ied, ldName);
            if (device != null) {
                result.addAll(device.lns());
                return result;
            }
            return null;
        }
        if (lnReference == null || lnReference.isEmpty() || !SclRefParser.isValid(lnReference))
            return null;
        SclRef sclRef = SclRefParser.parse(lnReference);
        SclLDevice device = findLd(ied, sclRef.ldInst());
        if (device != null) {
            SclLN ln = findLn(device, sclRef.lnName());
            if (ln != null) {
                result.add(ln);
                return result;
            }
        }
        return null;
    }

    /**
     * 反向查找：在指定 IED 中查找包含给定 LN 的 LD 实例名。
     *
     * @param ied
     *            IED 对象
     * @param ln
     *            要查找的 LN
     * @return LD 的 inst 值，未找到时返回 null
     */
    public static String findLdInst(SclIED ied, SclLN ln) {
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer server = ap.server();
            if (server != null) {
                for (SclLDevice ld : server.lDevices()) {
                    if (ld.findLnByFullName(ln.getFullName()) != null)
                        return ld.inst();
                }
            }
        }
        return null;
    }

    private static Navigator empty() {
        return new Navigator(null, null, null, null, null, null, null, null);
    }

    // ==================== 状态 ====================

    public boolean isValid() {
        return ln != null;
    }
    public boolean hasDoi() {
        return doi != null;
    }
    public boolean hasSdi() {
        return sdi != null;
    }
    public boolean hasDai() {
        return dai != null;
    }

    // ==================== 访问器 ====================

    public SclDocument document() {
        return document;
    }
    public SclIED ied() {
        return ied;
    }
    public SclLDevice ld() {
        return ld;
    }
    public SclLN ln() {
        return ln;
    }
    public SclDOI doi() {
        return doi;
    }
    public SclSDI sdi() {
        return sdi;
    }
    public SclDAI dai() {
        return dai;
    }
    public SclRef ref() {
        return ref;
    }

    public String daiValue() {
        if (dai == null || dai.vals().isEmpty())
            return null;
        return dai.vals().get(0).value();
    }
}
