package com.ysh.jcms.utils.scl2.navigate;

import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.ied.*;
import com.ysh.jcms.utils.scl2.model.instance.*;
import com.ysh.jcms.utils.scl2.ref.SclRef;
import com.ysh.jcms.utils.scl2.ref.SclRefParser;

/**
 * 引用 → 实例元素 导航器。
 * <p>
 * 顺着引用路径走，定位到模型中的实例元素（IED → LD → LN → DOI → SDI → DAI）。
 * 引用格式支持：
 * <ul>
 *   <li>{@code IEDName/LD/LN.DO.DA} — 完整路径</li>
 *   <li>{@code LD/LN.DO.DA} — 省略 IED 名（需指定作用域）</li>
 * </ul>
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

    // ==================== 工厂方法 ====================

    /** 在文档范围内按完整引用导航（格式：{@code IEDName/LD/LN.DO.DA}） */
    public static Navigator of(SclDocument document, String fullRef) {
        if (document == null || fullRef == null) return empty();
        int firstSlash = fullRef.indexOf('/');
        if (firstSlash <= 0) return empty();
        String iedName = fullRef.substring(0, firstSlash);
        String rest = fullRef.substring(firstSlash + 1);

        if (!SclRefParser.isValid(rest)) return empty();
        SclRef sclRef = SclRefParser.parse(rest);

        SclIED ied = document.findIedByName(iedName);
        if (ied == null) return empty();
        return navigate(ied, sclRef, document);
    }

    /** 在指定 IED 内按引用导航（格式：{@code LD/LN.DO.DA}） */
    public static Navigator of(SclIED ied, String ref) {
        if (ied == null || ref == null || !SclRefParser.isValid(ref)) return empty();
        SclRef sclRef = SclRefParser.parse(ref);
        return navigate(ied, sclRef, null);
    }

    /** 在指定 IED 内按已解析的 SclRef 导航 */
    public static Navigator of(SclIED ied, SclRef ref) {
        if (ied == null || ref == null) return empty();
        return navigate(ied, ref, null);
    }

    private static Navigator navigate(SclIED ied, SclRef sclRef, SclDocument doc) {
        // 找 LDevice
        SclLDevice ld = findLd(ied, sclRef.ldName());
        if (ld == null) return empty();

        // 找 LN
        SclLN ln = findLn(ld, sclRef.lnName());
        if (ln == null) return empty();

        // DO 级别以下才需要进 DOI
        if (sclRef.isLnLevel()) {
            return new Navigator(doc, ied, ld, ln, null, null, null, sclRef);
        }

        SclDOI doi = ln.findDoiByName(sclRef.doName());
        if (doi == null) return empty();

        if (sclRef.isDoLevel()) {
            return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        }

        // DA 或 SDI.BDA 级别
        // 逐段解析：从 rawRef 去掉 "LD/LN." 前缀，再去掉 "DO."，剩下的就是 SDI 链 + DA
        String raw = sclRef.rawRef();
        int firstDot = raw.indexOf('.');
        if (firstDot < 0) return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        String afterLn = raw.substring(firstDot + 1); // "DO.SDI.DA" 或 "DO.DA"
        int secondDot = afterLn.indexOf('.');
        if (secondDot < 0) return new Navigator(doc, ied, ld, ln, doi, null, null, sclRef);
        String afterDo = afterLn.substring(secondDot + 1); // "SDI.DA" 或 "DA"
        String[] parts = afterDo.split("\\.");

        if (parts.length == 1) {
            // 直接 DA
            SclDAI dai = doi.findDaiByName(parts[0]);
            if (dai == null) return empty();
            return new Navigator(doc, ied, ld, ln, doi, null, dai, sclRef);
        }

        // 有 SDI 链：逐层往下走
        SclSDI currentSdi = null;
        SclDAI resultDai = null;
        for (int i = 0; i < parts.length - 1; i++) {
            SclSDI next = (currentSdi == null)
                    ? doi.findSdiByName(parts[i])
                    : currentSdi.findSdiByName(parts[i]);
            if (next == null) return empty();
            currentSdi = next;
        }
        // 最后一段是 DA
        if (currentSdi != null) {
            resultDai = currentSdi.findDaiByName(parts[parts.length - 1]);
        }
        return new Navigator(doc, ied, ld, ln, doi, currentSdi, resultDai, sclRef);
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
            if (ln.getFullName().equals(lnName)) {
                return ln;
            }
        }
        return null;
    }

    private static Navigator empty() {
        return new Navigator(null, null, null, null, null, null, null, null);
    }

    // ==================== 状态判断 ====================

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

    /** 获取 DAI 的第一个 Val 值（快捷方法） */
    public String daiValue() {
        if (dai == null || dai.vals().isEmpty()) return null;
        return dai.vals().get(0).value();
    }
}
