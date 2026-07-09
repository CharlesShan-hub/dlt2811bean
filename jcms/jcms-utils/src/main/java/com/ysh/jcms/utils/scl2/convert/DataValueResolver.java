package com.ysh.jcms.utils.scl2.convert;

import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.instance.SclDAI;
import com.ysh.jcms.utils.scl2.model.instance.SclDOI;
import com.ysh.jcms.utils.scl2.model.instance.SclSDI;
import com.ysh.jcms.utils.scl2.model.template.SclDA;
import com.ysh.jcms.utils.scl2.model.template.SclDOType;
import com.ysh.jcms.utils.scl2.navigate.Navigator;
import com.ysh.jcms.utils.scl2.navigate.TypeChain;

/**
 * 数据值解析器。
 * <p>
 * 基于 {@link Navigator} + {@link TypeChain} 积木，按引用路径查找 DAI 值并追溯 bType。
 * 职责单一：只做"查值 + 查类型"，不做值转换。
 * <p>
 * 使用方式：
 * <pre>{@code
 * DataValueEntry dv = DataValueEntryResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
 * // dv.val() → "false", dv.bType() → "BOOLEAN"
 * }</pre>
 */
public final class DataValueResolver {

    private DataValueResolver() {}

    /**
     * 按完整引用解析数据值。
     *
     * @param doc  SCL 文档
     * @param ref  完整引用（格式：{@code IEDName/LD/LN.DO.DA}）
     * @return DataValueEntry，未找到返回 null
     */
    public static DataValueEntry resolve(SclDocument doc, String ref) {
        return resolve(doc, ref, null);
    }

    /**
     * 按完整引用解析数据值，支持 FC 过滤。
     *
     * @param doc SCL 文档
     * @param ref 完整引用（格式：{@code IEDName/LD/LN.DO} 或 {@code IEDName/LD/LN.DO.DA}）
     * @param fc  功能约束过滤（"ST"、"MX" 等），null 或 "XX" 表示不过滤
     * @return DataValueEntry，未找到返回 null
     */
    public static DataValueEntry resolve(SclDocument doc, String ref, String fc) {
        if (doc == null || ref == null) return null;

        Navigator nav = Navigator.go(doc, ref);
        if (!nav.isValid() || nav.ln() == null) return null;

        // LN 级别：无值可查
        if (nav.ref().isLnLevel()) return null;

        // DA 级别：直接取 DAI 值
        if (nav.ref().isDaLevel()) {
            if (nav.dai() == null) return null;
            String daiVal = firstVal(nav.dai());
            if (daiVal == null) return null;
            String bType = resolveBType(nav);
            return new DataValueEntry(ref, daiVal, bType);
        }

        // DO 级别：需要 FC 过滤
        return resolveDoLevel(nav, doc, ref, fc);
    }

    /** DO 级别的值查找（需要 FC 过滤来确定取哪个 DA） */
    private static DataValueEntry resolveDoLevel(Navigator nav, SclDocument doc, String ref, String fc) {
        SclDOI doi = nav.doi();
        if (doi == null) return null;

        boolean filterByFc = fc != null && !fc.isEmpty() && !"XX".equalsIgnoreCase(fc);

        if (!filterByFc) {
            // 无 FC 过滤：返回第一个有值的 DAI
            for (SclDAI dai : doi.dais()) {
                String val = firstVal(dai);
                if (val != null) {
                    String bType = resolveDaBType(doc, nav, dai.name());
                    return new DataValueEntry(ref, val, bType);
                }
            }
            return null;
        }

        // 有 FC 过滤：在 DOType 中找匹配 FC 的 DA，然后找对应的 DAI/SDI
        String lnTypeId = nav.ln().lnType();
        if (lnTypeId == null || doc.dataTypeTemplates() == null) return null;

        TypeChain chain = TypeChain.of(doc.dataTypeTemplates());
        TypeChain.DoTypeStep doTypeStep = chain.from(lnTypeId).doDef(nav.ref().doName());
        SclDOType doType = doTypeStep.doType();
        if (doType == null) return null;

        for (SclDA da : doType.das()) {
            if (!fc.equalsIgnoreCase(da.fc())) continue;

            // 先找 DAI
            SclDAI dai = doi.findDaiByName(da.name());
            if (dai != null) {
                String val = firstVal(dai);
                if (val != null) {
                    String bType = resolveDaBType(doc, nav, da.name());
                    return new DataValueEntry(ref, val, bType);
                }
            }

            // 再找 SDI
            SclSDI sdi = doi.findSdiByName(da.name());
            if (sdi != null) {
                for (SclDAI sdai : sdi.dais()) {
                    String val = firstVal(sdai);
                    if (val != null) {
                        String bType = resolveSdiBdaBType(doc, nav, da.name(), sdai.name());
                        return new DataValueEntry(ref, val, bType);
                    }
                }
            }
        }

        return null;
    }

    /** 获取 DAI 的第一个 Val 值 */
    private static String firstVal(SclDAI dai) {
        if (dai == null || dai.vals().isEmpty()) return null;
        String v = dai.vals().get(0).value();
        return (v != null && !v.isEmpty()) ? v : null;
    }

    /** DA 级别的 bType 解析（DO.DA → TypeChain） */
    private static String resolveDaBType(SclDocument doc, Navigator nav, String daName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null) return null;
        String ref = nav.ref().doName() + "." + daName;
        return TypeChain.of(doc.dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref);
    }

    /** SDI.BDA 级别的 bType 解析 */
    private static String resolveSdiBdaBType(SclDocument doc, Navigator nav, String sdiName, String bdaName) {
        if (doc.dataTypeTemplates() == null || nav.ln().lnType() == null) return null;
        String ref = nav.ref().doName() + "." + sdiName + "." + bdaName;
        return TypeChain.of(doc.dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref);
    }

    /** 从 Navigator 当前状态推断 bType，包含 SDI 链 */
    private static String resolveBType(Navigator nav) {
        if (!nav.ref().isDaLevel()) return null;
        StringBuilder ref = new StringBuilder(nav.ref().doName());
        for (String sdi : nav.ref().sdiChain()) ref.append(".").append(sdi);
        ref.append(".").append(nav.ref().daName());
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null) return null;
        return TypeChain.of(nav.document().dataTypeTemplates())
                .resolveBType(nav.ln().lnType(), ref.toString());
    }
}
