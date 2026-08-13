package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.SclVal;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.template.SclBDA;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 数据值解析器。
 * <p>
 * 基于 {@link Navigator} + {@link TypeChain} 积木，按引用路径查找 DAI 值并追溯 bType。 使用方式：
 *
 * <pre>
 * {
 *     &#64;code
 *     DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
 *     // dv.val() → "false", dv.bType() → "BOOLEAN"
 * }
 * </pre>
 */
public final class DataValueResolver {

    private static final Logger log = LoggerFactory.getLogger(DataValueResolver.class);

    private DataValueResolver() {
    }

    /**
     * 按完整引用解析数据值。
     */
    public static DataValueEntry resolve(SclDocument doc, String ref) {
        return resolve(doc, ref, null);
    }

    /**
     * 按完整引用 + FC 过滤解析数据值。
     */
    public static DataValueEntry resolve(SclDocument doc, String ref, String fc) {
        if (doc == null || ref == null)
            return null;
        Navigator nav = Navigator.go(doc, ref);
        return resolve(nav, fc, ref);
    }

    /** 基于已创建的 Navigator 解析数据值。 */
    public static DataValueEntry resolve(Navigator nav, String fc) {
        if (nav == null || !nav.isValid())
            return null;
        return resolve(nav, fc, nav.ref() != null ? nav.ref().fullReference() : "");
    }

    /** 内部实现 */
    private static DataValueEntry resolve(Navigator nav, String fc, String ref) {
        if (nav == null || !nav.isValid() || nav.ln() == null) {
            log.debug("resolve ref={}: nav invalid or ln=null (valid={})", ref, nav != null ? nav.isValid() : false);
            return null;
        }

        if (nav.ref().isLnLevel())
            return null;

        if (nav.ref().isDaLevel()) {
            String bType = resolveBType(nav);
            if (nav.dai() != null) {
                String daiVal = firstVal(nav.dai());
                if (daiVal != null)
                    return new DataValueEntry(ref, daiVal, bType);
            }
            // 实例 DAI 无值 → 模板 DA/BDA 默认值兜底（真实 SCD 常见值写在 DOType 模板里）
            String tpl = templateDefault(nav);
            if (tpl != null)
                return new DataValueEntry(ref, tpl, bType);
            log.debug("resolve ref={}: no instance nor template value", ref);
            return null;
        }

        return resolveDoLevel(nav, ref, fc);
    }

    /** DO 级别的值查找（需要 FC 过滤来确定取哪个 DA） */
    private static DataValueEntry resolveDoLevel(Navigator nav, String ref, String fc) {
        SclDOI doi = nav.doi();
        if (doi == null)
            return null;

        boolean filterByFc = fc != null && !fc.isEmpty() && !"XX".equalsIgnoreCase(fc);

        if (!filterByFc) {
            for (SclDAI dai : doi.dais()) {
                String val = firstVal(dai);
                if (val != null) {
                    String bType = resolveDaBType(nav, dai.name());
                    return new DataValueEntry(ref, val, bType);
                }
                // 实例 DAI 无值 → 模板 DA 默认值兜底
                String tpl = templateDefaultByName(nav, dai.name());
                if (tpl != null) {
                    String bType = resolveDaBType(nav, dai.name());
                    return new DataValueEntry(ref, tpl, bType);
                }
            }
            return null;
        }

        String lnTypeId = nav.ln().lnType();
        if (lnTypeId == null || nav.document().dataTypeTemplates() == null)
            return null;

        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        TypeChain.DoTypeStep doTypeStep = chain.from(lnTypeId).doDef(nav.ref().doName());
        SclDOType doType = doTypeStep.doType();
        if (doType == null)
            return null;

        for (SclDA da : doType.das()) {
            if (!fc.equalsIgnoreCase(da.fc()))
                continue;

            SclDAI dai = doi.findDaiByName(da.name());
            if (dai != null) {
                String val = firstVal(dai);
                if (val != null) {
                    String bType = resolveDaBType(nav, da.name());
                    return new DataValueEntry(ref, val, bType);
                }
            }
            // 实例 DAI 无值 → DOType 中 DA 的模板默认值兜底
            String tpl = firstTemplateVal(da.vals());
            if (tpl != null) {
                String bType = resolveDaBType(nav, da.name());
                return new DataValueEntry(ref, tpl, bType);
            }

            SclSDI sdi = doi.findSdiByName(da.name());
            if (sdi != null) {
                for (SclDAI sdai : sdi.dais()) {
                    String val = firstVal(sdai);
                    if (val != null) {
                        String bType = resolveSdiBdaBType(nav, da.name(), sdai.name());
                        return new DataValueEntry(ref, val, bType);
                    }
                    // 实例 SDI 的 DAI 无值 → DAType 中 BDA 模板默认值兜底
                    String bdaTpl = templateBdaDefault(nav, da.name(), sdai.name());
                    if (bdaTpl != null) {
                        String bType = resolveSdiBdaBType(nav, da.name(), sdai.name());
                        return new DataValueEntry(ref, bdaTpl, bType);
                    }
                }
            }
        }

        return null;
    }

    private static String firstVal(SclDAI dai) {
        if (dai == null || dai.vals().isEmpty())
            return null;
        String v = dai.vals().get(0).value();
        return (v != null && !v.isEmpty()) ? v : null;
    }

    /** 取模板 DA/BDA 默认值列表中的第一个有效值。 */
    private static String firstTemplateVal(List<SclVal> vals) {
        if (vals == null || vals.isEmpty())
            return null;
        String v = vals.get(0).value();
        return (v != null && !v.isEmpty()) ? v : null;
    }

    /**
     * DA 级模板默认值兜底：按完整引用沿 TypeChain 找到 DOType 的 DA（或 DAType 的 BDA）默认 Val。 支持无 SDI 的
     * DO.DA 与单级 SDI 的 DO.SDI.BDA 两种路径（与现有类型解析一致）。
     */
    private static String templateDefault(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        String doName = nav.ref().doName();
        String daName = nav.ref().daName();
        if (doName == null || daName == null)
            return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        if (nav.ref().sdiChain() == null || nav.ref().sdiChain().isEmpty()) {
            SclDA da = chain.from(nav.ln().lnType()).doDef(doName).daDef(daName).da();
            return da != null ? firstTemplateVal(da.vals()) : null;
        }
        TypeChain.DaStep step = chain.from(nav.ln().lnType()).doDef(doName).daDef(nav.ref().sdiChain().get(0));
        SclBDA bda = step.daType().bdaDef(daName);
        return bda != null ? firstTemplateVal(bda.vals()) : null;
    }

    /** DO 级（无 FC 过滤）按 DA 名查模板默认值。 */
    private static String templateDefaultByName(Navigator nav, String daName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null || nav.ref().doName() == null)
            return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        SclDA da = chain.from(nav.ln().lnType()).doDef(nav.ref().doName()).daDef(daName).da();
        return da != null ? firstTemplateVal(da.vals()) : null;
    }

    /** SDI 内 DAI 无值时的 BDA 模板默认值兜底。 */
    private static String templateBdaDefault(Navigator nav, String sdiName, String bdaName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        TypeChain.DaStep step = chain.from(nav.ln().lnType()).doDef(nav.ref().doName()).daDef(sdiName);
        SclBDA bda = step.daType().bdaDef(bdaName);
        return bda != null ? firstTemplateVal(bda.vals()) : null;
    }

    private static String resolveDaBType(Navigator nav, String daName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        String ref = nav.ref().doName() + "." + daName;
        return TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref);
    }

    private static String resolveSdiBdaBType(Navigator nav, String sdiName, String bdaName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        String ref = nav.ref().doName() + "." + sdiName + "." + bdaName;
        return TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref);
    }

    private static String resolveBType(Navigator nav) {
        if (!nav.ref().isDaLevel())
            return null;
        StringBuilder ref = new StringBuilder(nav.ref().doName());
        for (String sdi : nav.ref().sdiChain())
            ref.append(".").append(sdi);
        ref.append(".").append(nav.ref().daName());
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        return TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref.toString());
    }
}
