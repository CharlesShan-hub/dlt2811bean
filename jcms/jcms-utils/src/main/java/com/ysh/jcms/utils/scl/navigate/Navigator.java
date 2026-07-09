package com.ysh.jcms.utils.scl.navigate;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.*;
import com.ysh.jcms.utils.scl.model.instance.*;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;

/**
 * 引用导航器 —— SclRef → 模型元素。
 * <p>
 * 核心入口：{@link #go(SclDocument, SclRef)}。
 * 所有查找操作最终都通过 SclRef 定位到模型元素。
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

    private Navigator(SclDocument document, SclIED ied, SclLDevice ld, SclLN ln,
                      SclDOI doi, SclSDI sdi, SclDAI dai, SclRef ref) {
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
        if (document == null || ref == null) return empty();

        // 找 IED
        String iedName = ref.iedName();
        if (iedName == null) return empty();  // SclRef 必须含 IED 名
        SclIED ied = document.findIedByName(iedName);
        if (ied == null) return empty();

        return navigate(ied, ref, document);
    }

    /** 按引用字符串导航（便捷方法） */
    public static Navigator go(SclDocument document, String ref) {
        if (document == null || ref == null || !SclRefParser.isValid(ref)) return empty();
        return go(document, SclRefParser.parse(ref));
    }

    /** 在指定 IED 内按 SclRef 导航 */
    public static Navigator go(SclIED ied, SclRef ref) {
        if (ied == null || ref == null) return empty();
        return navigate(ied, ref, null);
    }

    /** 在指定 IED 内按字符串导航 */
    public static Navigator go(SclIED ied, String ref) {
        if (ied == null || ref == null || !SclRefParser.isValid(ref)) return empty();
        return navigate(ied, SclRefParser.parse(ref), null);
    }

    /** 在文档 + 指定 IED 内按字符串导航（带 dataTypeTemplates 访问能力） */
    public static Navigator go(SclDocument doc, SclIED ied, String ref) {
        if (doc == null || ied == null || ref == null || !SclRefParser.isValid(ref)) return empty();
        return navigate(ied, SclRefParser.parse(ref), doc);
    }

    // ==================== 导航逻辑 ====================

    private static Navigator navigate(SclIED ied, SclRef sclRef, SclDocument doc) {
        // 找 LDevice
        SclLDevice ld = findLd(ied, sclRef.ldInst());
        if (ld == null) return empty();

        // 找 LN
        SclLN ln = findLn(ld, sclRef.lnName());
        if (ln == null) return empty();

        // LN 级别
        if (sclRef.isLnLevel()) {
            return new Navigator(doc, ied, ld, ln, null, null, null, sclRef);
        }

        // DO 级别及以上：找 DOI
        SclDOI doi = ln.findDoiByName(sclRef.doName());
        if (doi == null) return empty();

        if (sclRef.isDoLevel()) {
            return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        }

        // DA 级别：走 SDI 链→DAI
        SclSDI currentSdi = null;
        for (String sdiName : sclRef.sdiChain()) {
            currentSdi = (currentSdi == null)
                    ? doi.findSdiByName(sdiName)
                    : currentSdi.findSdiByName(sdiName);
            if (currentSdi == null) return empty();
        }

        SclDAI dai = (currentSdi != null)
                ? currentSdi.findDaiByName(sclRef.daName())
                : doi.findDaiByName(sclRef.daName());
        if (dai == null) return empty();

        return new Navigator(doc, ied, ld, ln, doi, currentSdi, dai, sclRef);
    }

    private static SclLDevice findLd(SclIED ied, String ldInst) {
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer server = ap.server();
            if (server != null) {
                SclLDevice ld = server.findLDeviceByInst(ldInst);
                if (ld != null) return ld;
            }
        }
        return null;
    }

    private static SclLN findLn(SclLDevice ld, String lnName) {
        for (SclLN ln : ld.lns()) {
            if (ln.getFullName().equals(lnName)) return ln;
        }
        return null;
    }

    private static Navigator empty() {
        return new Navigator(null, null, null, null, null, null, null, null);
    }

    // ==================== 状态 ====================

    public boolean isValid() { return ln != null; }
    public boolean hasDoi() { return doi != null; }
    public boolean hasSdi() { return sdi != null; }
    public boolean hasDai() { return dai != null; }

    // ==================== 访问器 ====================

    public SclDocument document() { return document; }
    public SclIED ied() { return ied; }
    public SclLDevice ld() { return ld; }
    public SclLN ln() { return ln; }
    public SclDOI doi() { return doi; }
    public SclSDI sdi() { return sdi; }
    public SclDAI dai() { return dai; }
    public SclRef ref() { return ref; }

    public String daiValue() {
        if (dai == null || dai.vals().isEmpty()) return null;
        return dai.vals().get(0).value();
    }
}
