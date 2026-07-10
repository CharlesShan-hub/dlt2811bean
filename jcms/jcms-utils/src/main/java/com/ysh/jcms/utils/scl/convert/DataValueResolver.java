package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            if (nav.dai() == null) {
                log.debug("resolve ref={}: dai=null", ref);
                return null;
            }
            String daiVal = firstVal(nav.dai());
            if (daiVal == null) {
                log.debug("resolve ref={}: dai={} no vals, davals={}", ref, nav.dai().name(), nav.dai().vals().size());
                return null;
            }
            String bType = resolveBType(nav);
            return new DataValueEntry(ref, daiVal, bType);
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

            SclSDI sdi = doi.findSdiByName(da.name());
            if (sdi != null) {
                for (SclDAI sdai : sdi.dais()) {
                    String val = firstVal(sdai);
                    if (val != null) {
                        String bType = resolveSdiBdaBType(nav, da.name(), sdai.name());
                        return new DataValueEntry(ref, val, bType);
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
